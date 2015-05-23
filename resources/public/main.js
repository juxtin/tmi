// Something like this to get query variables.
function getQueryParam (variable, defaultValue) {
    // Find all URL parameters
    var query = location.search.substring (1);
    var vars = query.split ('&');
    for (var i = 0; i < vars.length; i++) {
        var pair = vars [i].split ('=');

        // If the query variable parameter is found, decode it to use and return it for use
        if (pair [0] === variable) {
            return decodeURIComponent (pair [1]);
        }
    }
    return defaultValue || false;
}

var return_to = getQueryParam ('return_to', 'pebblejs://close#');

document.getElementById ('myForm').onsubmit = function () {
    var some_settings = { 'stopID' : document.getElementById ('stopID').value };
    // Set the return URL depending on the runtime environment
    location.href = return_to + encodeURIComponent(JSON.stringify(some_settings));
    return false;
}
