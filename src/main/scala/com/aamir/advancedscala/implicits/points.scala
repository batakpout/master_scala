
/**
 * Implicits parameters is a way that allows parameters of a method to be found
 * The implicit scope which roughly consists of:
    -> local or inherited definitions;
    -> imported definitions;
    -> definitions in the companion object of the type class or the parameter type (in this case JsonWriter or String).
  * if the compiler sees multiple candidate definitions, it fails with an ambiguous implicit values
 */