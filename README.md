MyLocation

This driver is used in conjunction with HD+. This HD+ feature provides location and phone updates that can be used for updating dashboard location maps or any other device/app that utilizes geo coordinates.

v0.1.7 - Added presence capability.\
   There is a new preference variable that is to be the name of the zone to which presence will be set.\
   If the zone name received from HD+ EXACTLY matches the preference name then presence will be set.\  
   i.e.\
   HD+ reports "Home" and the preference is "Home" then 'present'\
   HD+ reports "Work" and the preference is "Home" then 'not present'\
   HD+ reports no zone then 'not present'

The driver has these attributes which are updated when a new location has been received from HD+.

latitude - Current latitude\
longitude - Current longitude\
accuracy - The accuracy of the location\
lastUpdated - The last update timestamp\
battery - battery level\
wifi - On wifi true/false\
charging - true/false\
status - activity reported by the phone (in a vehicle, walking, running, etc)\
confidence - confidence in % about the above status\
statusSet - the last time the status changed\
geoWKT - The lat and long in WKT format that can be used in google maps\
name - If configured on the device, this is the name of the presence zone the device currently resides
presence - present/not present
