package repositories.factories

/**
 * Created by Szymek Seget on 26.09.15.
 */
object ReflectionFactory {

  def build[T](className:String):Option[T] = {
    import scala.reflect.runtime.universe
    val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)
    try {
      val module = runtimeMirror.staticModule(className)
      val obj = runtimeMirror.reflectModule(module)
      val instance = obj.instance.asInstanceOf[T]
      Some(instance)
    }catch{
      case e:ScalaReflectionException => None
      case e => throw e
    }
  }
}
