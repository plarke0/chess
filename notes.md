# Notes

## 1/13/26
General discussion on the history of Java. It is a hybrid language that compiles into a JavaByte is the interpreted on
specific devices. Can be faster the C/C++ as it can dynamically compile certain code during runtime, rather than having
to guess in advance.

Also went over Phase 0 and getting started.

## 1/15/26
For phase0, don't worry about check/checkmate. You also don't need to worry about enpasant or castling.

Put all the common logic in the parent move calculator class.

In the move calculator, use the ChessPosition to find the piece type at that position on ChessBoard to create the
correct subclass of move calculator.

### Using Java from cmd

- Compile:
  - javac {name}.java
  - Produces {name}.class
  - Be in the directory of your java file
- Run:
  - java {name}.class

### JavaDoc
Example of documentation:
```java
/**
 * Description of class.
 * Additional description/examples
 */
public class MyClass{ 
    /**
     * Description of method.
     * Additional description/examples
     * @param ...
     * @return ...
     */
     public void myMethod() {
        
    }
}
```

### Primitive Datatypes

- byte (1 byte)
- short (2 bytes)
- __int__ (4 bytes)
- long (8 bytes)
- float (4 bytes)
- __double__ (8 bytes)
- char (2 bytes)
- __boolean__

#### Integrals
Literal integral primitives are always interpreted as `int`.
```java
long long1 = 10;
long long2 = 10L;
```
Because of this, `long1` actually takes slightly longer to initialize than `long2`.

#### Floats
Floating point literals are always interpreted as `double`.
```java
float aFloat = 2.5;
float bFloat = 2.5f;
double aDouble = 3.14;
```
`aFloat` will not compile, as doubles need to be cast to floats before being assigned to a float variable.

#### Converting Between Primitives and Strings

```java
int i = new Integer.parseInt(string);
```

#### Strings
- Declaration and Assignment
```java
String s1 = "Hello";
String s2 = new String("Hello");
```
Don't use `new` with strings
- Concatenation
```java
String s1 = "Hello";
String s2 = "BYU";
String s3 = s1 + " " + s2;
```
Inefficient for many strings, as it makes a new string for each step of the concatenation.
- Formatting
```java
String s1 = "Hello";
String s2 = "BYU";
String s3 = String.format("%s %s", s1, s2);
```

- Special Characters
  - `\n` newline
  - `\t` tab
  - `\"` double quote
  - `\'` single quote
  - `\\` backslash
  - `\b` backspace
  - `\uXXXX` Unicode for hex XXXX
  - `\r` carriage return
  - `\f` form feed


- String Builder

More efficient way to create strings from many parts

```java
public class Example {
  public static void main(String[] args) {
    StringBuilder builder = new StringBuilder();
    builder.append("Using ");
    builder.append("a ");
    builder.append("StringBuilder ");
    builder.append("is more efficient.");
    String str = builder.toString();
  }
}
```

