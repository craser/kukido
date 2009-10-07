// This is used as the body's onload event handler, and kicks off the
// request to Twitter's REST API.
function includeTwitterTimeline()
{
    // The callback parameter is given the name of the JavaScript
    // function we want called when the JSON data loads.  In this
    // case, we use displayTimeline.
    var url = "http://twitter.com/statuses/user_timeline.json?callback=displayTimeline&count=10&id=DeathB4Decaf";
    
    var script = document.createElement("script");
    script.setAttribute("src", url);
    // Hard-coding the ID is safe, since we're not running
    // concurrently with anything that might stomp on us.
    script.setAttribute("id", "twitterAjax"); 

    // Unless we actually add the <script> node to the document, it
    // will never load.
    var head = document.getElementsByTagName("head")[0];
    head.appendChild(script);
}

// Since you told Twitter the name of this function, it will call back
// here when user_timeline.json loads, and pass an object containing
// the data you asked for.
function displayTimeline(userTimeline)
{
    try {
        // Now that we've got our hands on the user's timeline, we have to
        // actually display it by modifying the DOM:
        var displayDiv = document.getElementById("twitterDisplay");
        displayDiv.innerHTML = formatTimeline(userTimeline);

        // Element ID has to agree with value from
        // includeTwitterTimeline() above.
        var twitterAjax = document.getElementById("twitterAjax");
        twitterAjax.parentNode.removeChild(twitterAjax);
    }
    catch (e) {
        // Do nothing.
    }
}

// This just builds an HTML table that we can use to display the
// given timeline.
function formatTimeline(userTimeline)
{
    var html = "";
    // A header with a title and profile image
    // Assumes that all tweets are for the same user.

    var profileUrl = formatProfileUrl(userTimeline[0].user.screen_name);
    var imgUrl = userTimeline[0].user.profile_image_url;

    html += "<div class=\"twitterProfile\">"
        + "<a href=\"" + profileUrl + "\">"
        //+ "<img class=\"twitterProfile\" src=\"http://twitter.com/favicon.ico\" />"
        + "<img class=\"twitterProfile\" src=\"" + imgUrl + "\" />"
        + "</a>"
        + "</div>";
    
    // The actual tweets
    for (var i = 0; i < userTimeline.length; i++) {
        html += formatTweet(userTimeline[i]);
    }
    return html;
}

function formatTweet(tweet) 
{
    var t = formatText(tweet);
    var d = formatDate(tweet.created_at);
    var l = formatTweetLink(tweet.user.screen_name, tweet.id);
    var html = "<div class=\"tweet\">" + t + "</div>"
        + "<div class=\"tweetDate\"><a href=\"" + l + "\">" + d + "</a></div>";
    return html;
}

function formatText(tweet)
{
    var wordFormatter = buildWordFormatter(tweet);
    var text = tweet.text.replace(/[^\s]+/g, wordFormatter);
    return text;
}

// Trying to avoid the rat's nest of regexps and if/else...
function buildWordFormatter(tweet)
{
    var namePattern = /@([\w_-]+)/g;
    var linkPattern = /(https?:\/\/[^\s]+)/g;

    var nameFormatter = buildNameFormatter(tweet, namePattern);
    var linkFormatter = formatLink;

    return function(word)
    {
        if (word.match(namePattern))
            return word.replace(namePattern, nameFormatter);
        else if (word.match(linkPattern))
            return word.replace(linkPattern, linkFormatter);
        else
            return word;
    }    
}

function buildNameFormatter(tweet, namePattern)
{   
    var matches = tweet.text.match(namePattern);
    var isReply = matches && matches.length == 1 && tweet.in_reply_to_status_id;

    return function(text, name)
    {   
        var url = "http://twitter.com/" + name;
        if (isReply) {
            url += "/statuses/" + tweet.in_reply_to_status_id;
        }
        return "@<a href=\"" + url + "\">" + name + "</a>";
    }
}

function formatLink(url) // Tweet is ignored for now.
{
    var link = url.replace(/^(.*)$/, "<a href=\"$1\">$1</a>");
    return link;
}

function formatDate(dateString) 
{
    // Twitter uses a non-standard date format: "Fri Oct 17 18:08:09
    // +0000 2008" So we have to re-arrange the time zone and year
    // into their standard locations.
    var d = new Date(dateString); // Try parsing as-is.
    if (!d.getTime()) { // Some browsers need help, some don't.
        var td = /(.*) ([+\-]\d+) (\d{4})/; // Re-arrange to standard format.
        var std = dateString.replace(td, "$1 $3 $2");
        d = new Date(std);
    }
    var days = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
    var day = days[d.getDay()];
    var hour = d.getHours();
    var min = d.getMinutes();
    var ampm = (hour >= 12) ? "pm" : "am";
    hour = (hour > 12) ? (hour % 12) : hour;
    min = (min < 10) ? "0" + min : min;
    
    var s = day + " at " + hour + ":" + min + " " + ampm;
    return s;
}


function formatTweetLink(userId, tweetId) 
{
    return "http://twitter.com/" + userId + "/statuses/" + tweetId;
}

function formatProfileUrl(userId) 
{
    return "http://twitter.com/" + userId;
}




    