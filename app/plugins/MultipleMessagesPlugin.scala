package play.api.i18n

import play.api._
import play.api.i18n._

import scala.collection.JavaConverters._

import scalax.file._
import scalax.io.JavaConverters._

/**
 * MessagesPlugin implementation that load i18n messages from multiple files on the classpath, thus allowing i18n messages 
 * to be split into separate, manageable files.
 *
 * To enable and configure this plugin, include the following properties in application.conf:
 *
 *  plugin.multiplemessages.enabled=true
 *  plugin.multiplemessages.additional.files="more-messages,even-more-messages"
 *
 * Messages will then be loaded from these additional files, as well as the standard messages.xxx files. 
 *
 * NOTE: This class is declared in package play.api.i18n because use of the MessagesParser is restricted to classes in
 * said package.  
 */
class MultipleMessagesPlugin(app: Application) extends MessagesPlugin(app) {

  /**
   * The configuration key that defines whether this plugin is enabled.
   */
  final val pluginKey = "plugin.multiplemessages.enabled"
  
  /**
   * The configuration key used to tell this plugin where additional messages can be found.
   */
  final val additionalFilesKey = "plugin.multiplemessages.additional.files"
  
  /**
   * The list of all messages files to be loaded.
   */
  lazy val filenames = app.configuration.getString(additionalFilesKey) match {
    case Some(list) => List("messages") ++ list.split(",")
    case _          => List("messages")
  }
   
  protected def loadMessages(file: String) : Map[String, String] = {
    app.classloader.getResources(file).asScala.toList.reverse.map { messageFile =>
      new Messages.MessagesParser(messageFile.asInput, messageFile.toString).parse.map { message =>
        message.key -> message.pattern
      }.toMap
    }.foldLeft(Map.empty[String, String]) { _ ++ _}
  }
  
  protected def messages = (Lang.availables(app)
    .map( l => (l.code, filenames.map(_.concat(".%s").format(l.code)))) :+ (("default", filenames)))
    .map( files => (files._1, files._2.flatMap(loadMessages(_)).toMap) )
    .toMap
    
  /**
   * The underlying internationalisation API.
   */
  override lazy val api = MessagesApi(messages)
  
  /**
   * Run this plugin if and only if it has been switched on in application configuration.
   */
  override lazy val enabled = {
    app.configuration.getString(pluginKey).filter(_ == "true").isDefined
  }
  
  /**
   * Loads all configuration and messages files defined in the classpath.
   */
  override def onStart = api
  
}
