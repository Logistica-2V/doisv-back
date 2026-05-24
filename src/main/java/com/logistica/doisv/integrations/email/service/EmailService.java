package com.logistica.doisv.integrations.email.service;

import com.logistica.doisv.core.enums.StatusSolicitacao;
import com.logistica.doisv.core.util.generation.DataUtil;
import com.logistica.doisv.modules.lojista.entity.Lojista;
import com.logistica.doisv.modules.solicitacao.entity.Solicitacao;
import com.logistica.doisv.modules.venda.entity.Venda;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.dominio.frontend.url}")
    private String urlBaseFrontend;

    private String obterCssBaseEmail() {
        return """
                body {
                    font-family: 'Poppins', Arial, sans-serif;
                    margin: 0; padding: 0;
                    background-color: #2e3034;
                    -webkit-text-size-adjust: 100%;
                }
                .container {
                    width: 100%; max-width: 600px;
                    margin: 30px auto;
                    background-color: #1e1f22;
                    border-radius: 16px;
                    overflow: hidden;
                    border: 1px solid #43464d;
                }
                .header {
                    padding: 30px 25px;
                    text-align: center;
                    background-color: #1e1f22;
                    border-bottom: 1px solid #43464d;
                }
                .header img { max-width: 100px; height: auto; margin-bottom: 12px; }
                .header h2 { margin: 0; color: #eeeeee; font-size: 20px; font-weight: 600; }
                .content {
                    padding: 35px; color: #eeeeee;
                    line-height: 1.7; font-size: 15px;
                }
                .content h1 { color: #eeeeee; font-size: 24px; font-weight: 700; margin-bottom: 20px; }
                .content p { margin: 14px 0; color: #eeeeee; }
                .text-secondary { color: #94a3b8 !important; }
                .credentials-box {
                    background-color: #2e3034;
                    border: 1px solid #43464d;
                    border-radius: 14px;
                    padding: 28px 20px; margin: 28px 0;
                    text-align: center;
                }
                .credentials-box p {
                    margin: 8px 0; font-size: 13px; color: #94a3b8;
                    font-weight: 600; text-transform: uppercase; letter-spacing: 1.5px;
                }
                .credential-item { margin: 18px 0; }
                .cred-label {
                    font-size: 12px; color: #94a3b8;
                    margin-bottom: 6px; text-transform: uppercase; letter-spacing: 1px;
                }
                .cred-value {
                    font-weight: bold; font-size: 18px; color: #eeeeee;
                    background-color: #1e1f22; padding: 12px 24px;
                    border-radius: 10px;
                    font-family: 'Courier New', Courier, monospace;
                    display: inline-block; margin-top: 4px;
                    letter-spacing: 1px; border: 1px solid #43464d;
                    max-width: 90%; word-wrap: break-word; overflow-wrap: break-word;
                }
                .cred-value a { color: #eeeeee !important; text-decoration: none !important; }
                .code-box {
                    background-color: #2e3034; border: 1px solid #43464d;
                    border-radius: 14px; padding: 28px 20px; margin: 28px 0;
                    text-align: center;
                }
                .code-box p {
                    margin: 8px 0; font-size: 13px; color: #94a3b8;
                    font-weight: 600; text-transform: uppercase; letter-spacing: 1.5px;
                }
                .code-value {
                    font-weight: bold; font-size: 32px; color: #eeeeee;
                    background-color: #1e1f22; padding: 14px 36px;
                    border-radius: 10px;
                    font-family: 'Courier New', Courier, monospace;
                    display: inline-block; margin-top: 10px;
                    letter-spacing: 8px; border: 1px solid #43464d;
                }
                .guidance-card {
                    background-color: #2e3034; border-radius: 12px;
                    padding: 20px; margin: 24px 0;
                    border-left: 4px solid #43464d;
                }
                .guidance-card h3 { color: #eeeeee; font-size: 16px; margin-top: 0; margin-bottom: 14px; font-weight: 600; }
                .guidance-card ul { margin: 10px 0; padding-left: 20px; }
                .guidance-card li { margin: 10px 0; color: #94a3b8; font-size: 14px; }
                .guidance-card li strong { color: #eeeeee; }
                .security-box {
                    background-color: #2e3034; border: 1px solid #43464d;
                    border-left: 4px solid #94a3b8;
                    border-radius: 10px; padding: 20px; margin: 24px 0;
                }
                .security-box h4 { color: #eeeeee; margin-top: 0; margin-bottom: 14px; font-size: 15px; font-weight: 600; }
                .security-box p { margin: 8px 0; color: #94a3b8; font-size: 13px; }
                .security-box p strong { color: #eeeeee; }
                .action-button { text-align: center; margin: 32px 0; }
                .action-button a {
                    background-color: #eeeeee; color: #1e1f22;
                    padding: 14px 36px; border-radius: 10px;
                    text-decoration: none; font-weight: 700; font-size: 15px;
                    display: inline-block;
                }
                .footer {
                    background-color: #1e1f22; padding: 22px;
                    text-align: center; font-size: 12px; color: #94a3b8;
                    border-top: 1px solid #43464d;
                }
                .footer p { margin: 4px 0; }
                .disclaimer { font-size: 11px; color: #636977; }
                """;
    }

    private String montarInicioEmail(String titulo) {
        return "<!DOCTYPE html><html lang=\"pt-br\"><head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>" + titulo + "</title>" +
                "<style>" + obterCssBaseEmail() + "</style>" +
                "</head><body><div class=\"container\">";
    }

    private String montarFimEmail() {
        return "</div></body></html>";
    }

    private String montarCabecalho(String logoSrc, String altText, String titulo) {
        return "<div class=\"header\">" +
                "<img src=\"" + logoSrc + "\" alt=\"" + altText + "\">" +
                "<h2>" + titulo + "</h2></div>";
    }

    private String montarRodape(String empresa, int ano, String aviso) {
        return "<div class=\"footer\">" +
                "<p>&copy; " + ano + " " + empresa + ". Todos os direitos reservados.</p>" +
                "<p class=\"disclaimer\">" + aviso + "</p></div>";
    }

    private String montarItemCredencial(String label, String valor) {
        return "<div class=\"credential-item\">" +
                "<div class=\"cred-label\">" + label + "</div>" +
                "<div class=\"cred-value\">" + valor + "</div></div>";
    }

    private String valorOuPadrao(String valor, String padrao) {
        return valor != null && !valor.isBlank() ? valor : padrao;
    }

    private String descricaoStatus(StatusSolicitacao statusSolicitacao) {
        return statusSolicitacao != null ? statusSolicitacao.getStatusSolicitacao() : "Nao informado";
    }

    private boolean possuiDadosEssenciaisSolicitacao(Solicitacao solicitacao) {
        return solicitacao != null &&
                solicitacao.getConsumidor() != null &&
                solicitacao.getVenda() != null &&
                solicitacao.getVenda().getLoja() != null &&
                solicitacao.getConsumidor().getEmail() != null &&
                !solicitacao.getConsumidor().getEmail().isBlank();
    }

    private String obterLogoLoja(Solicitacao solicitacao) {
        String logoLoja = solicitacao.getVenda().getLoja().getLogo();
        return logoLoja != null && !logoLoja.isBlank()
                ? "https://lh3.googleusercontent.com/d/" + logoLoja
                : "https://lh3.googleusercontent.com/d/1OAZrlZgYhXO-UzJLx9SZy6JgdLs6W4v2";
    }

    private String obterProdutoSolicitacao(Solicitacao solicitacao) {
        if (solicitacao.getItemVenda() != null && solicitacao.getItemVenda().getProduto() != null) {
            return valorOuPadrao(solicitacao.getItemVenda().getProduto().getDescricao(), "Produto nao informado");
        }
        return "Produto nao informado";
    }

    private String obterDataAtualizacaoSolicitacao(Solicitacao solicitacao) {
        return solicitacao.getDataAtualizacao() != null
                ? solicitacao.getDataAtualizacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : "Nao informada";
    }

    private String montarDetalhesSolicitacao(Solicitacao solicitacao) {
        String produto = obterProdutoSolicitacao(solicitacao);
        String tipoSolicitacao = solicitacao.getTipoSolicitacao() != null
                ? solicitacao.getTipoSolicitacao().getDescricao()
                : "Nao informado";
        String motivo = solicitacao.getMotivo() != null
                ? solicitacao.getMotivo().getDescricao()
                : "Nao informado";
        String quantidade = solicitacao.getQuantidade() != null
                ? solicitacao.getQuantidade().toString()
                : "Nao informada";
        String idVenda = solicitacao.getVenda().getId() != null
                ? solicitacao.getVenda().getId().toString()
                : "Nao informada";

        return "<div class=\"guidance-card\">" +
                "<h3>Detalhes da solicita&ccedil;&atilde;o</h3>" +
                "<ul>" +
                "<li><strong>Produto:</strong> " + produto + "</li>" +
                "<li><strong>Tipo:</strong> " + tipoSolicitacao + "</li>" +
                "<li><strong>Motivo:</strong> " + motivo + "</li>" +
                "<li><strong>Quantidade:</strong> " + quantidade + "</li>" +
                "<li><strong>Venda:</strong> " + idVenda + "</li>" +
                "</ul>" +
                "</div>";
    }

    @Async
    public void enviarEmailAcessoConsumidor(Venda venda, String senha) {
        try {
            if (venda == null || venda.getConsumidor() == null || venda.getLoja() == null) {
                return;
            }

            String urlLoginConsumidor = urlBaseFrontend + "/consumidor/login";
            String nomeLoja = venda.getLoja().getNome();
            String logoUrl = "https://lh3.googleusercontent.com/d/" + venda.getLoja().getLogo();
            String nomeConsumidor = venda.getConsumidor().getNome();
            String serial = venda.getSerialVenda();
            int anoAtual = DataUtil.hoje().getYear();

            String html = montarInicioEmail("Acesso para Troca ou Devolução - " + nomeLoja) +
                    montarCabecalho(logoUrl, "Logo de " + nomeLoja, nomeLoja) +
                    "<div class=\"content\">" +
                    "<h1>Olá, " + nomeConsumidor + ",</h1>" +
                    "<p>Agradecemos por comprar na loja <strong>" + nomeLoja
                    + "</strong>! Você pode realizar a troca ou devolução de sua compra utilizando o código e a senha exclusivos em nosso portal.</p>"
                    +
                    "<p class=\"text-secondary\">Utilize as credenciais abaixo para acessar a área dedicada e seguir com a sua solicitação:</p>"
                    +
                    "<div class=\"credentials-box\">" +
                    "<p>🔑 CREDENCIAIS DE ACESSO</p>" +
                    montarItemCredencial("SERIAL DE ACESSO", serial) +
                    montarItemCredencial("SENHA", senha) +
                    "</div>" +
                    "<div class=\"action-button\">" +
                    "<a href=\""+ urlLoginConsumidor +"\">Acessar Portal de Trocas</a>" +
                    "</div>" +
                    "<p style=\"margin-top: 28px;\" class=\"text-secondary\">Atenciosamente,<br>Equipe 2V Logística</p>"
                    +
                    "</div>" +
                    montarRodape("2V Logística", anoAtual,
                            "Este é um e-mail automático. Por favor, não o responda se não precisar de suporte.")
                    +
                    montarFimEmail();

            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            helper.setTo(venda.getConsumidor().getEmail());
            helper.setSubject("Acesso para Troca ou Devolução - " + venda.getLoja().getNome());
            helper.setText(html, true);
            mailSender.send(mensagem);
        } catch (Exception e) {
            System.err.println("Erro ao enviar email da venda ID: " + venda.getId());
        }
    }

    @Async
    public void enviarEmailRecuperacaoSenha(Lojista lojista, String codigoRecuperacao) throws MessagingException {
        String nomeLoja = lojista.getLoja().getNome();
        String logoUrl = "https://lh3.googleusercontent.com/d/" + lojista.getLoja().getLogo();
        String nomeLojista = lojista.getNome();
        int anoAtual = DataUtil.hoje().getYear();

        String html = montarInicioEmail("Recuperação de Senha - " + nomeLoja) +
                montarCabecalho(logoUrl, "Logo de " + nomeLoja, nomeLoja) +
                "<div class=\"content\">" +
                "<h1>Olá, " + nomeLojista + ",</h1>" +
                "<p>Recebemos uma solicitação de recuperação de senha para sua conta. Utilize o código de verificação abaixo para redefinir sua senha:</p>"
                +
                "<div class=\"code-box\">" +
                "<p>CÓDIGO DE VERIFICAÇÃO</p>" +
                "<span class=\"code-value\">" + codigoRecuperacao + "</span>" +
                "</div>" +
                "<div class=\"security-box\">" +
                "<h4>⚠️ Importante:</h4>" +
                "<p>• Este código é válido por <strong>30 minutos</strong></p>" +
                "<p>• Não compartilhe este código com ninguém</p>" +
                "<p>• Se você não solicitou esta recuperação, ignore este e-mail</p>" +
                "</div>" +
                "<p>Após inserir o código, você poderá criar uma nova senha de acesso ao sistema.</p>" +
                "<p style=\"margin-top: 28px;\" class=\"text-secondary\">Atenciosamente,<br>Equipe " + nomeLoja + "</p>"
                +
                "</div>" +
                montarRodape(nomeLoja, anoAtual,
                        "Este é um e-mail automático. Por favor, não o responda se não precisar de suporte.")
                +
                montarFimEmail();

        MimeMessage mensagem = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

        helper.setTo(lojista.getEmail());
        helper.setSubject("Recuperação de Senha");
        helper.setText(html, true);
        mailSender.send(mensagem);
    }

    @Async
    public void enviarEmailCadastroLoja(Lojista lojistaAdmin, String senha) throws MessagingException {
        String nomeLoja = lojistaAdmin.getLoja().getNome();
        String nomeLojista = lojistaAdmin.getNome();
        String emailLojista = lojistaAdmin.getEmail();
        String urlAcesso = urlBaseFrontend + "/lojista/login";
        String logoId = "1OAZrlZgYhXO-UzJLx9SZy6JgdLs6W4v2";
        String logoUrl = "https://lh3.googleusercontent.com/d/" + logoId;
        int anoAtual = DataUtil.hoje().getYear();

        String html = montarInicioEmail("Bem-vindo " + nomeLoja + " - Seu Primeiro Acesso") +
                montarCabecalho(logoUrl, "Logo " + nomeLoja, "2V Logística") +
                "<div class=\"content\">" +
                "<h1>Olá, " + nomeLojista + "! 🎉</h1>" +
                "<p>Seja muito bem-vindo ao <strong>" + nomeLoja
                + "</strong>! Estamos muito felizes em ter sua loja conosco. Sua conta foi criada com sucesso e preparamos um acesso especial para você começar a gerenciar seu negócio.</p>"
                +
                "<div class=\"credentials-box\">" +
                "<p>🔐 SUAS CREDENCIAIS DE ACESSO</p>" +
                montarItemCredencial("USUÁRIO (E-MAIL)", emailLojista) +
                montarItemCredencial("SENHA PROVISÓRIA", senha) +
                "</div>" +
                "<div class=\"guidance-card\">" +
                "<h3>📋 PRIMEIROS PASSOS OBRIGATÓRIOS:</h3>" +
                "<ul>" +
                "<li><strong>1. ALTERE SUA SENHA AGORA MESMO:</strong> Por segurança, você deve alterar esta senha provisória no seu primeiro acesso ao sistema.</li>"
                +
                "<li><strong>2. ACESSO ADMINISTRATIVO:</strong> Este usuário possui privilégios de administrador. Utilize-o apenas para configurações gerais e gerenciamento de outros usuários.</li>"
                +
                "<li><strong>3. CRIE SEU USUÁRIO DIÁRIO:</strong> Para operações do dia a dia, crie um usuário comum com permissões limitadas.</li>"
                +
                "</ul>" +
                "</div>" +
                "<div class=\"security-box\">" +
                "<h4>⚠️ RECOMENDAÇÕES IMPORTANTES DE SEGURANÇA:</h4>" +
                "<p>• <strong>NÃO COMPARTILHE ESTE ACESSO:</strong> Este usuário admin é pessoal e intransferível. Cada administrador deve ter seu próprio acesso.</p>"
                +
                "<p>• <strong>SENHA FORTE:</strong> Ao alterar sua senha, utilize uma combinação forte com letras maiúsculas, minúsculas, números e caracteres especiais.</p>"
                +
                "<p>• <strong>USUÁRIO DO DIA A DIA:</strong> Crie um usuário comum para atividades rotineiras como vendas, consultas e emissão de relatórios básicos.</p>"
                +
                "<p>• <strong>RESERVE O ADMIN:</strong> Mantenha este acesso para situações que realmente exijam permissões elevadas como cadastro de novos usuários, configurações do sistema e exclusão de dados.</p>"
                +
                "</div>" +
                "<div class=\"action-button\">" +
                "<a href=\"" + urlAcesso + "\" target=\"_blank\">🔑 ACESSAR O SISTEMA AGORA</a>" +
                "</div>" +
                "<p class=\"text-secondary\">Precisa de ajuda? Nossa equipe de suporte está pronta para auxiliá-lo no que for preciso.</p>"
                +
                "<p style=\"margin-top: 28px;\" class=\"text-secondary\">Atenciosamente,<br>Equipe <strong>2V</strong></p>"
                +
                "</div>" +
                montarRodape("2V Logística", anoAtual,
                        "Este é um e-mail automático. Por favor, não responda se não precisar de suporte.")
                +
                montarFimEmail();

        MimeMessage mensagem = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

        helper.setTo(lojistaAdmin.getEmail());
        helper.setSubject(String.format("Bem-vindo ao %s - Seu acesso foi criado!",
                lojistaAdmin.getLoja().getNome()));
        helper.setText(html, true);
        mailSender.send(mensagem);
    }

    @Async
    public void enviarEmailUsuarioMasterSuporte(String emailUsuarioMaster, String senha, String nomeLoja)
            throws MessagingException {
        String urlAcesso = "https://app.logistica.com.br/login";
        String logoId = "1OAZrlZgYhXO-UzJLx9SZy6JgdLs6W4v2";
        String logoUrl = "https://lh3.googleusercontent.com/d/" + logoId;
        int anoAtual = DataUtil.hoje().getYear();
        String dataCadastro = DataUtil.hoje().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String html = montarInicioEmail("Usuário Master Criado - " + nomeLoja) +
                montarCabecalho(logoUrl, "Logo 2V Logística", "2V Logística — Uso Interno") +
                "<div class=\"content\">" +
                "<h1>🛡️ Novo Usuário Master Criado</h1>" +
                "<p>Uma nova loja foi cadastrada no sistema e um <strong>usuário master de suporte</strong> foi gerado automaticamente. Este acesso é destinado exclusivamente à equipe de suporte para realização de ações administrativas e correções quando necessário.</p>"
                +
                "<div class=\"guidance-card\">" +
                "<h3>🏪 Informações da Loja</h3>" +
                "<ul>" +
                "<li><strong>Nome da Loja:</strong> " + nomeLoja + "</li>" +
                "<li><strong>Data de Cadastro:</strong> " + dataCadastro + "</li>" +
                "</ul>" +
                "</div>" +
                "<div class=\"credentials-box\">" +
                "<p>🔐 CREDENCIAIS DO USUÁRIO MASTER</p>" +
                montarItemCredencial("USUÁRIO (E-MAIL)", emailUsuarioMaster) +
                montarItemCredencial("SENHA MASTER", senha) +
                "</div>" +
                "<div class=\"security-box\">" +
                "<h4>⚠️ ATENÇÃO — USO RESTRITO AO SUPORTE:</h4>" +
                "<p>• <strong>ACESSO INTERNO:</strong> Estas credenciais são de uso exclusivo da equipe de suporte técnico. Não devem ser compartilhadas com o lojista.</p>"
                +
                "<p>• <strong>USO EMERGENCIAL:</strong> Utilize este acesso somente para correções, ajustes técnicos ou ações que não possam ser realizadas pelo próprio lojista.</p>"
                +
                "<p>• <strong>GUARDE COM SEGURANÇA:</strong> Armazene estas credenciais em local seguro e de acesso controlado pela equipe.</p>"
                +
                "<p>• <strong>RASTREABILIDADE:</strong> Toda ação realizada com este usuário ficará registrada nos logs do sistema. Utilize-o com responsabilidade.</p>"
                +
                "</div>" +
                "<div class=\"action-button\">" +
                "<a href=\"" + urlAcesso + "\" target=\"_blank\">🔑 ACESSAR O SISTEMA</a>" +
                "</div>" +
                "<p class=\"text-secondary\">Este e-mail foi gerado automaticamente no momento do cadastro da loja. Em caso de dúvidas, entre em contato com a equipe de desenvolvimento.</p>"
                +
                "<p style=\"margin-top: 28px;\" class=\"text-secondary\">Atenciosamente,<br>Sistema <strong>2V Logística</strong></p>"
                +
                "</div>" +
                montarRodape("2V Logística", anoAtual,
                        "Este é um e-mail automático de uso interno. Não responda este e-mail.")
                +
                montarFimEmail();

        MimeMessage mensagem = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

        helper.setTo("logistica.doisv@gmail.com");
        helper.setSubject(String.format("[SUPORTE] Usuário Master criado — Loja: %s", nomeLoja));
        helper.setText(html, true);
        mailSender.send(mensagem);
    }

    @Async
    public void enviarEmailSolicitacaoAprovada(Solicitacao solicitacao) {
        try {
            if (!possuiDadosEssenciaisSolicitacao(solicitacao)) {
                return;
            }

            String urlLoginConsumidor = urlBaseFrontend + "/consumidor/login";
            String nomeLoja = valorOuPadrao(solicitacao.getVenda().getLoja().getNome(), "Loja");
            String logoUrl = obterLogoLoja(solicitacao);
            String nomeConsumidor = valorOuPadrao(solicitacao.getConsumidor().getNome(), "cliente");
            String idSolicitacao = solicitacao.getId() != null ? solicitacao.getId().toString() : "Nao informado";
            String statusFinal = descricaoStatus(solicitacao.getStatusSolicitacao());
            String dataAtualizacao = obterDataAtualizacaoSolicitacao(solicitacao);
            int anoAtual = DataUtil.hoje().getYear();

            String html = montarInicioEmail("Solicitacao aprovada - " + nomeLoja) +
                    montarCabecalho(logoUrl, "Logo de " + nomeLoja, nomeLoja) +
                    "<div class=\"content\">" +
                    "<h1>Ol&aacute;, " + nomeConsumidor + ",</h1>" +
                    "<p>A solicita&ccedil;&atilde;o ID <strong>" + idSolicitacao
                    + "</strong> foi <strong>aprovada</strong> pela loja <strong>" + nomeLoja + "</strong>.</p>" +
                    "<p class=\"text-secondary\">Acesse o portal do consumidor para acompanhar os detalhes completos e os pr&oacute;ximos passos do atendimento.</p>" +
                    "<div class=\"guidance-card\">" +
                    "<h3>Resumo da aprova&ccedil;&atilde;o</h3>" +
                    "<ul>" +
                    "<li><strong>Status final:</strong> " + statusFinal + "</li>" +
                    "<li><strong>Data de atualiza&ccedil;&atilde;o:</strong> " + dataAtualizacao + "</li>" +
                    "<li><strong>Loja:</strong> " + nomeLoja + "</li>" +
                    "</ul>" +
                    "</div>" +
                    montarDetalhesSolicitacao(solicitacao) +
                    "<div class=\"action-button\">" +
                    "<a href=\"" + urlLoginConsumidor + "\" target=\"_blank\">Consultar solicita&ccedil;&atilde;o</a>" +
                    "</div>" +
                    "<p style=\"margin-top: 28px;\" class=\"text-secondary\">Atenciosamente,<br>Equipe " + nomeLoja + "</p>" +
                    "</div>" +
                    montarRodape(nomeLoja, anoAtual,
                            "Este &eacute; um e-mail autom&aacute;tico. Por favor, n&atilde;o responda se n&atilde;o precisar de suporte.") +
                    montarFimEmail();

            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            helper.setTo(solicitacao.getConsumidor().getEmail());
            helper.setSubject(String.format("Solicita\u00e7\u00e3o %s aprovada - %s", idSolicitacao, nomeLoja));
            helper.setText(html, true);
            mailSender.send(mensagem);
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de aprovacao da solicitacao ID: " +
                    (solicitacao != null ? solicitacao.getId() : "desconhecida"));
        }
    }

    @Async
    public void enviarEmailSolicitacaoReprovada(Solicitacao solicitacao, String motivoReprovacao) {
        try {
            if (!possuiDadosEssenciaisSolicitacao(solicitacao)) {
                return;
            }

            String urlLoginConsumidor = urlBaseFrontend + "/consumidor/login";
            String nomeLoja = valorOuPadrao(solicitacao.getVenda().getLoja().getNome(), "Loja");
            String logoUrl = obterLogoLoja(solicitacao);
            String nomeConsumidor = valorOuPadrao(solicitacao.getConsumidor().getNome(), "cliente");
            String idSolicitacao = solicitacao.getId() != null ? solicitacao.getId().toString() : "Nao informado";
            String statusFinal = descricaoStatus(solicitacao.getStatusSolicitacao());
            String dataAtualizacao = obterDataAtualizacaoSolicitacao(solicitacao);
            String motivoReprovacaoFormatado = valorOuPadrao(motivoReprovacao, "Motivo nao informado pela loja.");
            int anoAtual = DataUtil.hoje().getYear();

            String html = montarInicioEmail("Solicitacao reprovada - " + nomeLoja) +
                    montarCabecalho(logoUrl, "Logo de " + nomeLoja, nomeLoja) +
                    "<div class=\"content\">" +
                    "<h1>Ol&aacute;, " + nomeConsumidor + ",</h1>" +
                    "<p>A solicita&ccedil;&atilde;o ID <strong>" + idSolicitacao
                    + "</strong> foi <strong>reprovada</strong> pela loja <strong>" + nomeLoja + "</strong>.</p>" +
                    "<p class=\"text-secondary\">Confira o motivo informado pela loja e acesse o portal do consumidor para consultar os detalhes completos.</p>" +
                    "<div class=\"security-box\">" +
                    "<h4>Motivo da reprova&ccedil;&atilde;o</h4>" +
                    "<p>" + motivoReprovacaoFormatado + "</p>" +
                    "</div>" +
                    "<div class=\"guidance-card\">" +
                    "<h3>Resumo da reprova&ccedil;&atilde;o</h3>" +
                    "<ul>" +
                    "<li><strong>Status final:</strong> " + statusFinal + "</li>" +
                    "<li><strong>Data de atualiza&ccedil;&atilde;o:</strong> " + dataAtualizacao + "</li>" +
                    "<li><strong>Loja:</strong> " + nomeLoja + "</li>" +
                    "</ul>" +
                    "</div>" +
                    montarDetalhesSolicitacao(solicitacao) +
                    "<div class=\"action-button\">" +
                    "<a href=\"" + urlLoginConsumidor + "\" target=\"_blank\">Consultar solicita&ccedil;&atilde;o</a>" +
                    "</div>" +
                    "<p style=\"margin-top: 28px;\" class=\"text-secondary\">Atenciosamente,<br>Equipe " + nomeLoja + "</p>" +
                    "</div>" +
                    montarRodape(nomeLoja, anoAtual,
                            "Este &eacute; um e-mail autom&aacute;tico. Por favor, n&atilde;o responda se n&atilde;o precisar de suporte.") +
                    montarFimEmail();

            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            helper.setTo(solicitacao.getConsumidor().getEmail());
            helper.setSubject(String.format("Solicita\u00e7\u00e3o %s reprovada - %s", idSolicitacao, nomeLoja));
            helper.setText(html, true);
            mailSender.send(mensagem);
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de reprovacao da solicitacao ID: " +
                    (solicitacao != null ? solicitacao.getId() : "desconhecida"));
        }
    }

    @Async
    public void enviarEmailAtualizacaoSolicitacao(Solicitacao solicitacao, StatusSolicitacao statusAnterior){
        try {
            if (solicitacao == null || solicitacao.getConsumidor() == null || solicitacao.getVenda() == null ||
                    solicitacao.getVenda().getLoja() == null ||
                    solicitacao.getConsumidor().getEmail() == null ||
                    solicitacao.getConsumidor().getEmail().isBlank()) {
                return;
            }

            String urlLoginConsumidor = urlBaseFrontend + "/consumidor/login";
            String nomeLoja = valorOuPadrao(solicitacao.getVenda().getLoja().getNome(), "Loja");
            String logoLoja = solicitacao.getVenda().getLoja().getLogo();
            String logoUrl = logoLoja != null && !logoLoja.isBlank()
                    ? "https://lh3.googleusercontent.com/d/" + logoLoja
                    : "https://lh3.googleusercontent.com/d/1OAZrlZgYhXO-UzJLx9SZy6JgdLs6W4v2";
            String nomeConsumidor = valorOuPadrao(solicitacao.getConsumidor().getNome(), "cliente");
            String idSolicitacao = solicitacao.getId() != null ? solicitacao.getId().toString() : "Nao informado";
            String idVenda = solicitacao.getVenda().getId() != null ? solicitacao.getVenda().getId().toString() : "Nao informada";
            String produto = "Produto nao informado";
            if (solicitacao.getItemVenda() != null && solicitacao.getItemVenda().getProduto() != null) {
                produto = valorOuPadrao(solicitacao.getItemVenda().getProduto().getDescricao(), produto);
            }
            String tipoSolicitacao = solicitacao.getTipoSolicitacao() != null
                    ? solicitacao.getTipoSolicitacao().getDescricao()
                    : "Nao informado";
            String motivo = solicitacao.getMotivo() != null
                    ? solicitacao.getMotivo().getDescricao()
                    : "Nao informado";
            String quantidade = solicitacao.getQuantidade() != null
                    ? solicitacao.getQuantidade().toString()
                    : "Nao informada";
            String dataAtualizacao = solicitacao.getDataAtualizacao() != null
                    ? solicitacao.getDataAtualizacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : "Nao informada";
            String observacao = valorOuPadrao(solicitacao.getObservacao(),
                    "Nenhuma observacao complementar informada.");
            String statusAntigo = descricaoStatus(statusAnterior);
            String statusNovo = descricaoStatus(solicitacao.getStatusSolicitacao());
            int anoAtual = DataUtil.hoje().getYear();

            String html = montarInicioEmail("Solicitacao atualizada - " + nomeLoja) +
                    montarCabecalho(logoUrl, "Logo de " + nomeLoja, nomeLoja) +
                    "<div class=\"content\">" +
                    "<h1>Ol&aacute;, " + nomeConsumidor + ",</h1>" +
                    "<p>A solicita&ccedil;&atilde;o ID <strong>" + idSolicitacao
                    + "</strong> recebeu uma atualiza&ccedil;&atilde;o realizada pela loja <strong>" + nomeLoja
                    + "</strong>.</p>" +
                    "<p class=\"text-secondary\">Confira abaixo o resumo da altera&ccedil;&atilde;o. Para consultar todos os detalhes e acompanhar os pr&oacute;ximos passos, acesse o portal do consumidor.</p>" +
                    "<div class=\"guidance-card\">" +
                    "<h3>Resumo da atualiza&ccedil;&atilde;o</h3>" +
                    "<ul>" +
                    "<li><strong>Status antigo:</strong> " + statusAntigo + "</li>" +
                    "<li><strong>Status novo:</strong> " + statusNovo + "</li>" +
                    "<li><strong>Data de atualiza&ccedil;&atilde;o:</strong> " + dataAtualizacao + "</li>" +
                    "<li><strong>Loja:</strong> " + nomeLoja + "</li>" +
                    "</ul>" +
                    "</div>" +
                    "<div class=\"guidance-card\">" +
                    "<h3>Detalhes da solicita&ccedil;&atilde;o</h3>" +
                    "<ul>" +
                    "<li><strong>Produto:</strong> " + produto + "</li>" +
                    "<li><strong>Tipo:</strong> " + tipoSolicitacao + "</li>" +
                    "<li><strong>Motivo:</strong> " + motivo + "</li>" +
                    "<li><strong>Quantidade:</strong> " + quantidade + "</li>" +
                    "<li><strong>Venda:</strong> " + idVenda + "</li>" +
                    "<li><strong>Observa&ccedil;&atilde;o:</strong> " + observacao + "</li>" +
                    "</ul>" +
                    "</div>" +
                    "<div class=\"action-button\">" +
                    "<a href=\"" + urlLoginConsumidor + "\" target=\"_blank\">Consultar atualiza&ccedil;&atilde;o</a>" +
                    "</div>" +
                    "<p style=\"margin-top: 28px;\" class=\"text-secondary\">Atenciosamente,<br>Equipe " + nomeLoja + "</p>" +
                    "</div>" +
                    montarRodape(nomeLoja, anoAtual,
                            "Este &eacute; um e-mail autom&aacute;tico. Por favor, n&atilde;o responda se n&atilde;o precisar de suporte.") +
                    montarFimEmail();

            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            helper.setTo(solicitacao.getConsumidor().getEmail());
            helper.setSubject(String.format("Solicitação %s atualizada - %s", idSolicitacao, nomeLoja));
            helper.setText(html, true);
            mailSender.send(mensagem);
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de atualizacao da solicitacao ID: " +
                    (solicitacao != null ? solicitacao.getId() : "desconhecida"));
        }

    }
}
