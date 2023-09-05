/*     
       Revisions
            0.1.0 - 02Sep23 - Initial Release
            0.1.1 - 02Sep23R1 - Enhanced by @jpage4500
            	Thanks for the update
            0.1.2 - 05Sep23 - Added Activity and geoWKT 
*/
 
import groovy.json.*

metadata {
    definition(name: "My Location", namespace: "myLocation", author: "MarK Weninger") {
        capability "Actuator"
        capability "Battery"
        capability "Power Source"

        command('setLocation', [[name: 'Set Location', type: 'JSON_OBJECT', description: 'JSON format: {"lat":80.123, "lng":-80.123, "acc":50, "bat":10, "wifi":true, "power":true}']])

    }

    preferences {
        input name: "enlog", type: "bool", title: "Enable Logging", description: "", required: true
    }

    attribute "latitude", "number"
    attribute "longitude", "number"
    attribute "accuracy", "number"
    attribute "lastUpdated", "date"

    attribute "battery", "number"
    attribute "charging", "enum", ["true","false"]
    attribute "wifi", "enum", ["true","false"]
    attribute "activity", "string"
    attribute "geoWKT", "string"
    
}

def setLocation (loc) {
    if (enlog) {log.info "Location received ${loc}"}
    def jsonSlurper = new JsonSlurper()
    try {
        def locJson = jsonSlurper.parseText(loc)

        if (locJson.lat) sendEvent(name: "latitude", value: locJson.lat, displayed: true)
        if (locJson.lng) sendEvent(name: "longitude", value: locJson.lng, displayed: true)
        sendEvent (name: "geoWKT", value: "POINT (${locJson.lat} ${locJson.lng})", displayed: true)
        if (locJson.acc) sendEvent(name: "accuracy", value: locJson.acc, displayed: true)
        if (locJson.bat) sendEvent(name: "battery", value: locJson.bat, displayed: true)
        if (locJson.containsKey("wifi")) sendEvent(name: "wifi", value: locJson.wifi, displayed: true)
        if (locJson.containsKey("power")) sendEvent(name: "charging", value: locJson.power, displayed: true)
        if (locJson.containsKey("activ")) {
            act = "Invalid" /* Init activity to Invalid */
            switch (locJson.activ) {
                case 0:
                    act = "In a Vehicle"
                    break
                case 1:
                    act = "On a Bicycle"
                    break
                case 2:
                    act = "On Foot"
                    break
                case 3:
                    act = "Still"
                    break
                case 4:
                    act = "Unknown"
                    break
                case 5:
                    act = "Tilting"
                    break
                case 7:
                    act = "Walking"
                    break
                case 8:
                    act = "Running"
                    break
                default:
                    act = "Invalid"
            }
            sendEvent(name: "activity", value: act, displayed: true)
        }

        sendEvent(name: "lastUpdated", value: new Date())

    } catch (Exception ex) {
        log.warn "Caught Exception ${ex}"
    }
}
