colors = [
    "#9F5F9F", // Blue Violet
    "#FF7F00", // Coral
    "#42426F", // Corn Flower
    "#9932CD", // * Dark Orchid
    "#871F78", // * Dark Purple
    "#6B238E", // * Dark Slate Blue
    "#7093DB", // Dark Turquoise
    "#D19275", // Feldspar
    "#8E2323", // * Firebrick
    "#E47833", // Mandarian Orange
    "#8E236B", // * Maroon
    "#23238E", // * Navy Blue
    "#4D4DFF", // * Neon Blue
    "#FF6EC7", // Neon Pink
    "#00009C", // New Midnight Blue
    "#FF7F00", // Orange
    "#FF2400", // Orange Red
    "#DB70DB", // * Orchid
    "#5959AB", // Rich Blue
    "#6F4242", // Salmon
    "#8C1717", // * Scarlet
    "#8E6B23", // Sienna
    "#E6E8FA", // Silver
    "#3299CC", // Sky Blue
    "#007FFF", // Slate Blue
    "#FF1CAE", // Spicy Pink
    "#236B8E", // Steel Blue
    "#38B0DE", // Summer Sky
    "#D8BFD8", // Thistle
    "#4F2F4F", // Violet
    "#CC3299", // Violet Red
];

function getColor(i) {
    return colors[i % colors.length];
}

function getDefaultColor() {
    return "#FF0000";
}

function getColorByGradient(point) // g is a floating point number indicating the grade of the trail
{
    try {
    var grade = parseFloat(point.grade);
    // Assume a range of -10 to 10, then figure where on that scale we are.
    if (grade < -10) { grade = -10; }
    if (grade > 10)  { grade = 10; }
    grade += 10; // Convert to a 0-20 range
    var pct = 0.7; //grade/20;
    var green = Math.round(255 - (pct * 255));
    var red = Math.round(pct * 255);
    var r = red.toString(16);
    var g = green.toString(16);
    var b = "00";
    var color = formatColor(r, g, b);
    return color;
    }
    catch (e) {
        return "#000000"
    }
}

function formatColor(r, g, b)
{
    while (r.length < 2) r = "0" + r;
    while (g.length < 2) g = "0" + g;
    while (b.length < 2) b = "0" + b;
    
    return "#" + r + g + b;
}
