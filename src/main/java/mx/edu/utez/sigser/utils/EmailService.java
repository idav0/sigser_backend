package mx.edu.utez.sigser.utils;

import jakarta.mail.internet.MimeMessage;
import mx.edu.utez.sigser.controllers.email.dto.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
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
                    helper.setSubject("SIGSER - Recuperación de contraseña Servicio de RepairApp");
                    break;
                case "newUser":
                    helper.setSubject("SIGSER - Bienvenido a Servicio de Reparación de Equipos");
                    break;
                case "changeStatus-received":
                    helper.setSubject("SIGSER - Actualización de estado de reparación - Ingresado");
                    break;
                case "changeStatus-diagnosis":
                    helper.setSubject("SIGSER - Actualización de estado de reparación - En Diagnóstico");
                    break;
                case "changeStatus-quotation":
                    helper.setSubject("SIGSER - Actualización de estado de reparación - En Cotización");
                    break;
                case "changeStatus-waitingforcustomerapproval":
                    helper.setSubject("SIGSER - Actualización de estado de reparación - En Espera de aceptación del cliente");
                    break;
                case "changeStatus-waitingforparts":
                    helper.setSubject("SIGSER - Actualización de estado de reparación - En Espera de piezas");
                    break;
                case "changeStatus-repairing":
                    helper.setSubject("SIGSER - Actualización de estado de reparación - En Reparación");
                    break;
                case "changeStatus-waitingforcollection":
                    helper.setSubject("SIGSER - Actualización de estado de reparación - Listo para Entrega");
                    break;
                case "changeStatus-repaired":
                    helper.setSubject("SIGSER - Actualización de estado de reparación - Entregado");
                    break;
            }

            helper.addInline("logo", new ClassPathResource("static/images/Logo.png"));
            helper.setTo(email.getEmail());
            helper.setText(generateEmailContent(email, type), true);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateEmailContent(EmailDto email, String type) throws IOException {
        switch (type) {
            case "forgotPassword":

                String templatePath = "src/main/resources/templates/TokenPassword.html";
                try {
                    // Leer el contenido del template HTML
                    Path path = Paths.get(templatePath);
                    String content = Files.readString(path, StandardCharsets.UTF_8);

                    // Reemplazar los marcadores con los datos específicos
                    content = content.replace("{{email}}", email.getEmail());
                    content = content.replace("{{dig1}}", String.valueOf(email.getToken().charAt(0)));
                    content = content.replace("{{dig2}}", String.valueOf(email.getToken().charAt(1)));
                    content = content.replace("{{dig3}}", String.valueOf(email.getToken().charAt(2)));
                    content = content.replace("{{dig4}}", String.valueOf(email.getToken().charAt(3)));
                    content = content.replace("{{dig5}}", String.valueOf(email.getToken().charAt(4)));
                    content = content.replace("{{dig6}}", String.valueOf(email.getToken().charAt(5)));

                    return content;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ""; // Manejar el error apropiadamente
                }

            case "newUser":


                try {
                    InputStream inputStream = new ClassPathResource("templates/TokenPassword.html").getInputStream();
                    String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    // Leer el contenido del template HTML
//                    Path path = Paths.get(templatePath2);
//                    String content = Files.readString(path, StandardCharsets.UTF_8);

                    // Reemplazar los marcadores con los datos específicos
                    content = content.replace("{{nombre}}", email.getNombre());
                    content = content.replace("{{tmpcontra}}", email.getTmpcontra());

                    return content;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ""; // Manejar el error apropiadamente
                }

            case "changeStatus-received":
                String templatePath3 = "src/main/resources/templates/StatusReceived.html";
                try {
                    // Leer el contenido del template HTML
                    Path path = Paths.get(templatePath3);
                    String content = Files.readString(path, StandardCharsets.UTF_8);

                    // Reemplazar los marcadores con los datos específicos
                    content = content.replace("{{equipo}}", email.getEquipo());
                    content = content.replace("{{nombre}}", email.getNombre());

                    return content;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ""; // Manejar el error apropiadamente
                }

            case "changeStatus-diagnosis":
                String templatePath4 = "src/main/resources/templates/StatusDiagnosis.html";
                try {
                    // Leer el contenido del template HTML
                    Path path = Paths.get(templatePath4);
                    String content = Files.readString(path, StandardCharsets.UTF_8);

                    // Reemplazar los marcadores con los datos específicos
                    content = content.replace("{{equipo}}", email.getEquipo());
                    content = content.replace("{{nombre}}", email.getNombre());

                    return content;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ""; // Manejar el error apropiadamente
                }

            case "changeStatus-quotation":
                String templatePath5 = "src/main/resources/templates/QuotationRequest.html";
                try {
                    // Leer el contenido del template HTML
                    Path path = Paths.get(templatePath5);
                    String content = Files.readString(path, StandardCharsets.UTF_8);

                    // Reemplazar los marcadores con los datos específicos
                    content = content.replace("{{monto}}", email.getEquipo());
                    content = content.replace("{{nombre}}", email.getNombre());
                    content = content.replace("{{descripcion}}", email.getDescripcion());
                    content = content.replace("{{equipo}}", email.getEquipo());

                    return content;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ""; // Manejar el error apropiadamente
                }

            case "changeStatus-waitingforcustomerapproval":
                String templatePath6 = "src/main/resources/templates/StatusWaitingForCustomerApproval.html";
                try {
                    // Leer el contenido del template HTML
                    Path path = Paths.get(templatePath6);
                    String content = Files.readString(path, StandardCharsets.UTF_8);

                    // Reemplazar los marcadores con los datos específicos
                    content = content.replace("{{equipo}}", email.getEquipo());
                    content = content.replace("{{nombre}}", email.getNombre());

                    return content;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ""; // Manejar el error apropiadamente
                }

            case "changeStatus-waitingforparts":
                String templatePath7 = "src/main/resources/templates/StatusWaitingForParts.html";
                try {
                    // Leer el contenido del template HTML
                    Path path = Paths.get(templatePath7);
                    String content = Files.readString(path, StandardCharsets.UTF_8);

                    // Reemplazar los marcadores con los datos específicos
                    content = content.replace("{{equipo}}", email.getEquipo());
                    content = content.replace("{{nombre}}", email.getNombre());

                    return content;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ""; // Manejar el error apropiadamente
                }

            case "changeStatus-repairing":
                String templatePath8 = "src/main/resources/templates/StatusRepairing.html";
                try {
                    // Leer el contenido del template HTML
                    Path path = Paths.get(templatePath8);
                    String content = Files.readString(path, StandardCharsets.UTF_8);

                    // Reemplazar los marcadores con los datos específicos
                    content = content.replace("{{equipo}}", email.getEquipo());
                    content = content.replace("{{nombre}}", email.getNombre());

                    return content;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ""; // Manejar el error apropiadamente
                }

            case "changeStatus-waitingforcollection":
                String templatePath9 = "src/main/resources/templates/StatusWaitingForCollection.html";
                try {
                    // Leer el contenido del template HTML
                    Path path = Paths.get(templatePath9);
                    String content = Files.readString(path, StandardCharsets.UTF_8);

                    // Reemplazar los marcadores con los datos específicos
                    content = content.replace("{{equipo}}", email.getEquipo());
                    content = content.replace("{{nombre}}", email.getNombre());

                    return content;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ""; // Manejar el error apropiadamente
                }

            case "changeStatus-repaired":
                String templatePath10 = "src/main/resources/templates/StatusRepaired.html";
                try {
                    // Leer el contenido del template HTML
                    Path path = Paths.get(templatePath10);
                    String content = Files.readString(path, StandardCharsets.UTF_8);

                    // Reemplazar los marcadores con los datos específicos
                    content = content.replace("{{equipo}}", email.getEquipo());
                    content = content.replace("{{nombre}}", email.getNombre());

                    return content;
                } catch (Exception e) {
                    e.printStackTrace();
                    return ""; // Manejar el error apropiadamente
                }

            default:
                return "";


        }
    }
}


/*

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
 */