package trafegoEspacial.servico;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ProcessadorMensagem {

	public static final String CHAVE_BREADCRUMBLINK_VIEW = "template.breadcrumb.link.view";
	public static final String CHAVE_BREADCRUMBLINK_NOME = "template.breadcrumb.link.nome";

	private MessageSource messageSourceChave;
	private MessageSource messageSource;

	private Map<String, String> getCustomMensagemCodeMap(final MessageSource messageSource) {
		return new AbstractMap<String, String>() {

			@Override
			public String get(Object key) {
				try {
					return messageSource.getMessage((String) key, null, null);
				} catch (NoSuchMessageException ex) {
					return (String) key;
				}
			}

			@Override
			public Set<java.util.Map.Entry<String, String>> entrySet() {
				return null;
			}
		};
	}

	public String resolveChave(String chave) {
		return resolveChave(messageSourceChave, chave);
	}

	public String resolveChave(MessageSource messageSourceChave, String chave) {
		return messageSourceChave.getMessage(chave, new Object[0], null);
	}

	public String resolveExpressaoChave(String chave, Map<String, Object> dados) {
		return resolveExpressaoChave(messageSourceChave, chave, messageSource, dados);
	}

	public String resolveExpressaoChave(MessageSource messageSourceChave, String chave, MessageSource messageSource,
			Map<String, Object> dados) {
		String mensagem = resolveChave(messageSourceChave, chave);
		return resolveExpressao(mensagem, messageSource, dados);
	}

	public String resolveExpressao(String mensagem, Map<String, Object> dados) {
		return resolveExpressao(mensagem, messageSource, dados);
	}

	public String resolveExpressao(String mensagem, MessageSource messageSource, Map<String, Object> dados) {
		SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
		if (messageSource != null) {
			dados.put("customMensagem", getCustomMensagemCodeMap(messageSource));
		}
		StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
		standardEvaluationContext.setVariables(dados);
		return (String) spelExpressionParser.parseExpression(mensagem, new TemplateParserContext())
				.getValue(standardEvaluationContext);
	}

	public MessageSource getMessageSourceChave() {
		return messageSourceChave;
	}

	public void setMessageSourceChave(MessageSource messageSourceChave) {
		this.messageSourceChave = messageSourceChave;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
