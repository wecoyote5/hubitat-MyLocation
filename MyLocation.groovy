/*     
       Revisions
            0.1.0 - 02Sep23 - Initial Release
            0.1.1 - 02Sep23R1 - Enhanced by @jpage4500
            	Thanks for the update
            0.1.2 - 05Sep23 - Added Status and geoWKT
            0.1.3 - 06Sep23 - Shortened variables and separated accuracy
            0.1.4 - 07Sep23 - Added name attribute, converted all key checks to use 'containsKey'
*/
 
import groovy.json.*

metadata {
    definition(name: "My Location", namespace: "myLocation", author: "MarK Weninger/Joe Page", importUrl: "https://raw.githubusercontent.com/wecoyote5/hubitat-MyProjects/main/MyLocation.groovy") {
        capability "Actuator"
        capability "Battery"
        capability "Power Source"

        command('setLocation', [[name: 'Set Location', type: 'JSON_OBJECT', description: 'JSON format: {"lat":80.123, "lng":-80.123, "acc":50, "bat":10, "w":1, "p":1, "s":"driving", "c":50}']])

    }

    preferences {
        input name: "enlog", type: "bool", title: "Enable Logging", description: "", required: true
    }

    attribute "latitude", "number"
    attribute "longitude", "number"
    attribute "accuracy", "number"
    attribute "lastUpdated", "date"
    attribute "name", "string"                          // if configured by the user, this is name of the presence zone (i.e. 'home' or 'work') the device is currently in.

    attribute "battery", "number"
    attribute "charging", "enum", ["true","false"]
    attribute "wifi", "enum", ["true","false"]

    attribute "status", "string"                        // what the the device is doing ("driving")
    attribute "confidence", "string"                    // how confident that status is ("75%")
    attribute "statusSet", "date"                       // last time status changed (NOTE: not always the same as lastUpdated)
    
    attribute "geoWKT", "string"
}

def setLocation (loc) {
    if (enlog) {log.info "Location received ${loc}"}
    def jsonSlurper = new JsonSlurper()
    try {
        def locJson = jsonSlurper.parseText(loc)

        if (locJson.containsKey("lat")) {
            sendEvent(name: "latitude", value: locJson.lat)
            
            if (locJson.containsKey("lng")) {
                sendEvent(name: "longitude", value: locJson.lng)
                sendEvent (name: "geoWKT", value: "POINT (${locJson.lat} ${locJson.lng})") //Only update if both lat and long were received
            }
        }
        if (locJson.containsKey("acc")) sendEvent(name: "accuracy", value: locJson.acc)
        if (locJson.containsKey("bat")) sendEvent(name: "battery", value: locJson.bat)

        if (locJson.containsKey("w")) sendEvent(name: "wifi", value: locJson.w == 1 ? "true" : "false")
        if (locJson.containsKey("p")) sendEvent(name: "charging", value: locJson.p == 1 ? "true" : "false")
        if (locJson.containsKey("s")) sendEvent(name: "status", value: locJson.s)
        if (locJson.containsKey("c")) sendEvent(name: "confidence", value: locJson.c + "%")
        if (locJson.containsKey("ss")) sendEvent(name: "statusSet", value: new Date(locJson.ss))

        if (locJson.containsKey("l")) {
            sendEvent(name: "name", value: locJson.l)
        } else {
            sendEvent(name: "name", value: null)
        }        
        
        sendEvent(name: "lastUpdated", value: new Date())

    } catch (Exception ex) {
        log.warn "Caught Exception ${ex}"
    }
}
