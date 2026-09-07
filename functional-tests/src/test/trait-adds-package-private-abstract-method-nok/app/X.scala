package bar

class X extends foo.T { def m = 1 } // outside foo, yet it implements the private[foo] member
