// Important: Don't forget to include the call to System.loadLibrary
// as shown at the bottom of this code sample.
package whyq.activity;

import whyq.chilkatsoft.CkRsa;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SimpleActivity extends Activity {
  // Called when the activity is first created.
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView tv = new TextView(this);
    String outStr = "";

    CkRsa rsa = new CkRsa();

    boolean success;
    success = rsa.UnlockComponent("Anything for 30-day trial");
    if (success != true) {
        outStr += "RSA component unlock failed" + "\n";
        tv.setText(outStr);
        setContentView(tv);
        return;
    }

    //  This example also generates the public and private
    //  keys to be used in the RSA encryption.
    //  Normally, you would generate a key pair once,
    //  and distribute the public key to your partner.
    //  Anything encrypted with the public key can be
    //  decrypted with the private key.  The reverse is
    //  also true: anything encrypted using the private
    //  key can be decrypted using the public key.

    //  Generate a 1024-bit key.  Chilkat RSA supports
    //  key sizes ranging from 512 bits to 4096 bits.
    success = rsa.GenerateKey(1024);
    if (success != true) {
        outStr += rsa.lastErrorText() + "\n";
        tv.setText(outStr);
        setContentView(tv);
        return;
    }

    //  Keys are exported in XML format:
    String publicKey;
    publicKey = rsa.exportPublicKey();
    String privateKey;
    privateKey = rsa.exportPrivateKey();

    String plainText;
    plainText = "Encrypting and decrypting should be easy!";

    //  Start with a new RSA object to demonstrate that all we
    //  need are the keys previously exported:
    CkRsa rsaEncryptor = new CkRsa();

    //  Encrypted output is always binary.  In this case, we want
    //  to encode the encrypted bytes in a printable string.
    //  Our choices are "hex", "base64", "url", "quoted-printable".
    rsaEncryptor.put_EncodingMode("hex");

    //  We'll encrypt with the public key and decrypt with the private
    //  key.  It's also possible to do the reverse.
    rsaEncryptor.ImportPublicKey(publicKey);

    boolean usePrivateKey;
    usePrivateKey = false;
    String encryptedStr;
    encryptedStr = rsaEncryptor.encryptStringENC(plainText,usePrivateKey);
    outStr += encryptedStr + "\n";

    //  Now decrypt:
    CkRsa rsaDecryptor = new CkRsa();

    rsaDecryptor.put_EncodingMode("hex");
    rsaDecryptor.ImportPrivateKey(privateKey);

    usePrivateKey = true;
    String decryptedStr;
    decryptedStr = rsaDecryptor.decryptStringENC(encryptedStr,usePrivateKey);

    outStr += decryptedStr + "\n";
    tv.setText(outStr);
    setContentView(tv);
  }

//  static {
//      // Important: Make sure the name passed to loadLibrary matches the shared library
//      // found in your project's libs/armeabi directory.
//      //  for "libchilkat.so", pass "chilkat" to loadLibrary
//      //  for "libchilkatemail.so", pass "chilkatemail" to loadLibrary
//      //  etc.
//      // 
//      System.loadLibrary("chilkatrsa");
//
//      // Note: If the incorrect library name is passed to System.loadLibrary,
//      // then you will see the following error message at application startup:
//      //"The application <your-application-name> has stopped unexpectedly. Please try again."
//  }
}
