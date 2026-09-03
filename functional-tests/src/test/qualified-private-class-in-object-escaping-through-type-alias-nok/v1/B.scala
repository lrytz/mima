package foo

// the alias and the object it names sit in different files, so the pickle
// refers to Holder from outside, where it looks like a package
object OtherFile {
  type L = Holder.Inner
}
