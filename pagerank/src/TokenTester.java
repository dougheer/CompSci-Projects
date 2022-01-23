import java.io.IOException;

public class TokenTester {

  public static void main(String[] args) throws IOException {
    tokenizer token = new tokenizer("tokenization-input-part-B.txt");
    token.tokenize();
  }

}
