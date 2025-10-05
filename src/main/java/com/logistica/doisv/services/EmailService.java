package com.logistica.doisv.services;

import com.logistica.doisv.entities.Venda;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmailAcessoConsumidor(Venda venda) throws MessagingException {

        String corpoHtmlEmail = """
                <!DOCTYPE html>
                <html lang="pt-br">
                <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Acesso para Troca ou Devolução - %s</title>
                <style>
                    /* Estilos Gerais */
                    body {
                        font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
                        margin: 0;
                        padding: 0;
                        background-color: #f1f4f8;
                    }
                    .container {
                        width: 100%%;
                        max-width: 600px;
                        margin: 20px auto;
                        background-color: #ffffff;
                        border-radius: 12px;
                        overflow: hidden;
                        box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                        border: 1px solid #dee2e6;
                    }
                
                    /* Cabeçalho */
                    .header {
                        padding: 25px;
                        text-align: center;
                        background-color: #f8f9fa;
                        border-bottom: 1px solid #dee2e6;
                    }
                    .header img {
                        max-width: 120px;
                        height: auto;
                        margin-bottom: 10px;
                    }
                    .header h2 {
                        margin: 0;
                        color: #343a40;
                        font-size: 22px;
                        font-weight: 600;
                    }
                
                    /* Conteúdo */
                    .content {
                        padding: 35px;
                        color: #000000;
                        line-height: 1.7;
                        font-size: 16px;
                    }
                    .content h1 {
                        color: #212529;
                        font-size: 26px;
                        font-weight: 700;
                        margin-bottom: 20px;
                    }
                    .content p {
                        margin: 15px 0;
                    }
                
                    /* Caixa de Credenciais (Tons de Azul) */
                    .credentials-box {
                        background-color: #5583e7;
                        border-left: 5px solid #3c6adb;
                        padding: 20px;
                        margin: 30px 0;
                        text-align: center;
                    }
                    .credentials-box p {
                        margin: 12px 0;
                        font-size: 15px;
                        color: #ffffff;
                    }
                    .credentials-box span {
                        font-weight: bold;
                        font-size: 14px;
                        color: #5583e7;
                        background-color: #ffffff;
                        padding: 8px 15px;
                        border-radius: 6px;
                        font-family: 'Courier New', Courier, monospace;
                        display: inline-block;
                        margin-left: 8px;
                        border: 1px solid #e0e0e0;
                    }
                
                    /* Botão (Gradiente Azul) */
                    .button {
                        display: inline-block;
                        background-color: #5583e7;
                        color: #ffffff;
                        padding: 15px 35px;
                        text-align: center;
                        text-decoration: none;
                        border-radius: 8px;
                        margin-top: 20px;
                        font-size: 16px;
                        font-weight: bold;
                        transition: transform 0.2s, box-shadow 0.2s;
                    }
                    .button:hover {
                        transform: scale(1.05);
                        box-shadow: 0 4px 10px rgba(77, 208, 225, 0.4); /* Sombra sutil no hover */
                    }
                
                    /* Rodapé */
                    .footer {
                        background-color: #f8f9fa;
                        padding: 20px;
                        text-align: center;
                        font-size: 13px;
                        color: #6c757d;
                        border-top: 1px solid #dee2e6;
                    }
                    .footer p {
                        margin: 5px 0;
                    }
                    .disclaimer {
                        font-size: 11px;
                        color: #adb5bd;
                    }
                </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <img src="https://lh3.googleusercontent.com/d/%s" alt="Logo de %s">
                            <h2>%s</h2>
                        </div>
                        <div class="content">
                            <h1>Olá, %s,</h1>
                            <p>Agradecemos por comprar na loja %s! Você pode realizar a troca ou devolução de sua compra utilizando o código e a senha exclusivos em nosso portal.</p>
                            <p>Por favor, utilize as credenciais abaixo para acessar a área dedicada e seguir com a sua solicitação:</p>
                            <div class="credentials-box">
                                <p><strong>Serial de Acesso:</strong><span>%s</span></p>
                                <p><strong>Senha:</strong><span>%s</span></p>
                            </div>
                            <p>Clique no botão abaixo para ser direcionado ao portal e prosseguir:</p>
                            <a href="%s" class="button">Acessar Portal de Trocas</a>
                            <p style="margin-top: 30px;">Atenciosamente,<br>Equipe %s</p>
                        </div>
                        <div class="footer">
                            <p>© %d %s. Todos os direitos reservados.</p>
                            <p class="disclaimer">Este é um e-mail automático. Por favor, não o responda se não precisar de suporte.</p>
                        </div>
                    </div>
                </body>
                </html>
                """;

        MimeMessage mensagem = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

        helper.setTo(venda.getConsumidor().getEmail());
        helper.setSubject("Acesso para Troca ou Devolução - " + venda.getLoja().getNome());

        String corpoEmail = String.format(corpoHtmlEmail,
                venda.getLoja().getNome(),
                venda.getLoja().getLogo(),
                venda.getLoja().getNome(),
                venda.getLoja().getNome(),
                venda.getConsumidor().getNome(),
                venda.getLoja().getNome(),
                venda.getSerialVenda(),
                venda.getConsumidor().getCpf_cnpj().substring(0,4) + "@" + LocalDate.now().getYear(),
                "google.com",
                "2V Logística",
                LocalDate.now().getYear(),
                "2V Logística"
        );

        helper.setText(corpoEmail, true);
        mailSender.send(mensagem);
    }
}
