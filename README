Modularising i18n messages in a Play 2.x application
====================================================

This is a simple Play application that demonstrates how i18n messages can be split up into separate manageable files and then 
loaded by a custom Play plugin. Whilst this application uses version 2.2 of the Play Framework, the approach taken by this 
demo should work for all version 2.x applications.

### Modularised files

In this example application, i18n messages are split in the standard `conf/messages` file and a custom `conf/footer-messages` 
file. 

### Custom messages plugin

Messages are then loaded from these locations by `app/plugins/MultipleMessagesPlugin.scala`. This plugin is registered in
`conf/play.plugins` as follows:

  ```
  99:play.api.i18n.MultipleMessagesPlugin
  ```
  
Note that we give it a load number < 100 so that it runs before Play's default MessagesPlugin implementation.

Finally, the plugin is configured in `conf/application.conf`:

  ```
  # Plugin configuration
  # ~~~~~
  plugin.multiplemessages.enabled=true
  plugin.multiplemessages.additional.files="footer-messages"
  ``` 
