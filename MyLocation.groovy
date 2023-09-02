/*     
       Revisions
            02Sep23 - Initial Release
            02Sep23R1 - Enhanced by @jpage4000
            	Thanks for the update
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
}

def setLocation (loc) {
    if (enlog) {log.info "Location received ${loc}"}
    def jsonSlurper = new JsonSlurper()
    try {
        def locJson = jsonSlurper.parseText(loc)

        if (locJson.lat) sendEvent(name: "latitude", value: locJson.lat, displayed: true)
        if (locJson.lng) sendEvent(name: "longitude", value: locJson.lng, displayed: true)
        if (locJson.acc) sendEvent(name: "accuracy", value: locJson.acc, displayed: true)
        if (locJson.bat) sendEvent(name: "battery", value: locJson.bat, displayed: true)
        if (locJson.containsKey("wifi")) sendEvent(name: "wifi", value: locJson.wifi, displayed: true)
        if (locJson.containsKey("power")) sendEvent(name: "charging", value: locJson.power, displayed: true)

        sendEvent(name: "lastUpdated", value: new Date())

    } catch (Exception ex) {
        log.warn "Caught Exception ${ex}"
    }
}
