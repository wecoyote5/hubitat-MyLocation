/*     
       Revisions
            02Sep23 - Initial Release
*/
 
metadata {
   definition(name: "My Location", namespace: "myLocation", author: "MarK Weninger") {

      capability "Actuator"

      command "setLocation", ["STRING"]
   }

   preferences {
      input name: "enlog", type: "bool", title: "Enable Logging", description: "", required: true
   }
    
   attribute "myLat", "number"
   attribute "myLon", "number"
   attribute "myAcc", "number"
   attribute "lastUpdated", "date"
  
}

def setLocation (loc) {

    if (enlog) {log.info "Location received ${loc}"}
    try{
       String [] str
       str = loc.split('_')
    
       sendEvent(name: "myLat", value: Double.parseDouble(str[0]), displayed: true)
       sendEvent(name: "myLon", value: Double.parseDouble(str[1]), displayed: true)
       sendEvent(name: "myAcc", value: Double.parseDouble(str[2]), displayed: true)
       sendEvent(name: "lastUpdated", value: new Date().format("ddMMMyyyy HH:mm:ss"), displayed: true )
        
    } catch (Exception ex) {
        log.warn "Caught Exception ${ex}"
    }
}
