public final class App {
  static final class Mine implements foo.T {
    public int a() { return 1; }
  }

  public static void main(String[] args) {
    System.out.println(foo.Lib.doIt(new Mine()));
  }
}
