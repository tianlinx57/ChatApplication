package fr.utc.sr03.chat;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class tes {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // This should be the plaintext password
        String plainPassword = "tlx200057";

        // This should be the hashed password from the database (for example)
        String hashedPassword = "$2a$10$BSMvsHSGGi9vSWt/Ro4hJuAN6W40gTRmWxR9cANTJuEvqMBS9QlmC";

        // This should return true if the passwords match
        boolean isMatch = passwordEncoder.matches(plainPassword, hashedPassword);
        System.out.println("Password matches: " + isMatch);
    }
}
