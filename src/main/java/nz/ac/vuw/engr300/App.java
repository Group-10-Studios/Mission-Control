package nz.ac.vuw.engr300;

public class App {
  public boolean getStatus() {
    return true;
  }
  
  public void printHello() {
    System.out.println("Hello World!");
  }
  
  public static void main(String[] args) {
    new App().printHello();
  }
}
