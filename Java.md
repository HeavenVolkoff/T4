##Java Best Practices and Metrics Docs

####Use of Interface and Abstract Class
[http://stackoverflow.com/questions/10040069/abstract-class-vs-interface-in-java](Original Post (StackOverflow))

######When To Use Interfaces

>An interface allows somebody to start from scratch to implement your interface or implement your interface in some other code whose original or primary purpose was quite different from your interface. To them, your interface is only incidental, something that have to add on to the their code to be able to use your package. The disadvantage is every method in the interface must be public. You might not want to expose everything.

######When To Use Abstract classes

>An abstract class, in contrast, provides more structure. It usually defines some default implementations and provides some tools useful for a full implementation. The catch is, code using it must use your class as the base. That may be highly inconvenient if the other programmers wanting to use your package have already developed their own class hierarchy independently. In Java, a class can inherit from only one base class.

######When to Use Both

>You can offer the best of both worlds, an interface and an abstract class. Implementors can ignore your abstract class if they choose. The only drawback of doing that is calling methods via their interface name is slightly slower than calling them via their abstract class name.

[http://mindprod.com/jgloss/interfacevsabstract.html](More Info)