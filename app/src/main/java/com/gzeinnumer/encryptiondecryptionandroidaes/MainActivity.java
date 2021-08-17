package com.gzeinnumer.encryptiondecryptionandroidaes;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gzeinnumer.encryptiondecryptionandroidaes.databinding.ActivityMainBinding;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        binding.btnEn.setOnClickListener(v -> {
            String encryptedValue = binding.edEncrypted.getText().toString();
            String key = binding.edKey.getText().toString();
            try {
                String en = encrypt(encryptedValue, key);
                binding.edDecrypted.setText(en);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.btnDe.setOnClickListener(v -> {
            String decryptedValue = binding.edDecrypted.getText().toString();
            String key = binding.edKey.getText().toString();
            String en = null;
            try {
                en = decrypt(decryptedValue, key);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Wrong Password : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            binding.edDecryptedToEncrypted.setText(en);
        });
        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edKey.requestFocus();
                binding.edKey.setText("");
                binding.edEncrypted.setText("");
                binding.edDecrypted.setText("");
                binding.edDecryptedToEncrypted.setText("");
            }
        });
    }

    private String decrypt(String outputString, String key) throws Exception {
        SecretKeySpec keySpec = generateKey(key);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue); //decryptedValue
    }

    private String encrypt(String data, String key) throws Exception {
        SecretKeySpec keySpec = generateKey(key);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.encodeToString(encVal, Base64.DEFAULT); //encrypted
    }

    private SecretKeySpec generateKey(String key) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes, 0, bytes.length);
        byte[] keySecurity = digest.digest();
        return new SecretKeySpec(keySecurity, AES);
    }
}