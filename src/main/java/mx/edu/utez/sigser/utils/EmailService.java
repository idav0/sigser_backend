package mx.edu.utez.sigser.utils;

import jakarta.mail.internet.MimeMessage;
import mx.edu.utez.sigser.controllers.email.dto.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    public boolean sendMail(EmailDto email, String type) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            switch (type) {
                case "forgotPassword":
                    helper.setSubject("Recuperación de contraseña Servicio de Reparación de Equipos - SIGSER");
                    helper.addInline("tokenimg", new ClassPathResource("static/images/EmailTokenImg.png"));
                    break;
                case "newUser":
                    helper.setSubject("Bienvenido a Servicio de Reparación de Equipos - SIGSER");
                    helper.addInline("tokenimg", new ClassPathResource("static/images/EmailTokenImg.png"));
                    break;
                case "changeStatus":
                    helper.setSubject("Actualización de estado de reparación - SIGSER");
                    break;
            }

            helper.setTo(email.getEmail());
            helper.setText(generateEmailContent(email, type), true);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateEmailContent(EmailDto email, String type) {
        String templatePath = "src/main/resources/templates/TokenPassword.html";
        try {
            // Leer el contenido del template HTML
            Path path = Paths.get(templatePath);
            String content = Files.readString(path, StandardCharsets.UTF_8);

            // Reemplazar los marcadores con los datos específicos
            content = content.replace("{{nombre}}", email.getEmail());
            content = content.replace("{{comentarios}}", email.getToken());

            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return ""; // Manejar el error apropiadamente
        }
    }
}
