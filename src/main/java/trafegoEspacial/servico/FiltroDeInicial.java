package trafegoEspacial.servico;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class FiltroDeInicial implements Serializable {

	private static final long serialVersionUID = -7682320834531990104L;

//	public Map<String, String> montaJsonStringMap(InicialFiltroDados filtro) throws IOException {
//		String jsonString = montaJsonString(filtro);
//
//		ObjectMapper mapper = new ObjectMapper();
//		Map<String, String> jsonMap = mapper.readValue(jsonString, new TypeReference<Map<String, String>>() {});
//
//		return jsonMap;
//	}
//
//	public String montaJsonString(InicialFiltroDados filtro) throws IOException {
//		Map<String, Object> jsonMap = montaJson(filtro);
//
//		ObjectMapper mapper = new ObjectMapper();
//		return mapper.writeValueAsString(jsonMap);
//	}
//
//	public Map<String, Object> montaJson(InicialFiltroDados filtro) throws IOException {
//		Map<String, Object> jsonMap = new HashMap<String, Object>();
//
//		jsonMap.put("iniciaisFilter", montaDigestoFilter(filtro));
//		if (filtro.getClientGroup() != null) {
//			jsonMap.put("clientGroup", filtro.getClientGroup().getClientGroupId().toString());
//		}
//
//		return jsonMap;
//	}
//
//	private String montaDigestoFilter(InicialFiltroDados filtro) throws IOException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T00:00:00Z'");
//		HashMap<String, Object> eventoJson = new HashMap<String, Object>();
//		// Evento
//		if (filtro.getDataBpoDe() != null || filtro.getDataBpoAte() != null) {
//			HashMap<String, Object> dataJson = new HashMap<String, Object>();
//			if (filtro.getDataBpoDe() != null) {
//				Calendar calendar = new GregorianCalendar();
//				calendar.setTime(filtro.getDataBpoDe());
//				calendar.add(Calendar.DAY_OF_MONTH, -1);
//				dataJson.put("$gte", sdf.format(calendar.getTime()));
//			}
//			if (filtro.getDataBpoAte() != null) {
//				dataJson.put("$lte", sdf.format(filtro.getDataBpoAte()));
//			}
//			eventoJson.put("data_bpo", dataJson);
//		}
//		if (filtro.getEvento().getLiberado() != null && filtro.getEvento().getLiberado()) {
//			eventoJson.put("liberado", filtro.getEvento().getLiberado());
//		}
//		if (filtro.getIgnorado() == null || filtro.getIgnorado()) {
//			notExists(eventoJson, "ignorado");
//		}
//		// Iniciais
//		HashMap<String, Object> iniciaisJson = new HashMap<String, Object>();
//		if (filtro.getPossuiAudiencia() == null || !filtro.getPossuiAudiencia()) {
//			notExists(iniciaisJson, "audiencias");
//		}
//		if (filtro.getInicial() != null) {
//			if (!StringUtils.isEmpty(filtro.getInicial().getArea())) {
//				if (TIPO_AREAOUTROS.equals(filtro.getInicial().getArea())) {
//					HashMap<String, Object> ninJson = new HashMap<String, Object>();
//					ninJson.put("$nin", areasDigesto());
//					iniciaisJson.put("area", ninJson);
//				} else {
//					iniciaisJson.put("area", filtro.getInicial().getArea());
//				}
//			}
//			if (!StringUtils.isEmpty(filtro.getInicial().getTribunal())) {
//				iniciaisJson.put("tribunal", filtro.getInicial().getTribunal());
//			}
//			if (!StringUtils.isEmpty(filtro.getInicial().getUf())) {
//				iniciaisJson.put("uf", filtro.getInicial().getUf());
//			}
//			if (filtro.getDistribuicaoDataDe() != null || filtro.getDistribuicaoDataAte() != null) {
//				HashMap<String, Object> dataJson = new HashMap<String, Object>();
//				if (filtro.getDistribuicaoDataDe() != null) {
//					Calendar calendar = new GregorianCalendar();
//					calendar.setTime(filtro.getDistribuicaoDataDe());
//					calendar.add(Calendar.DAY_OF_MONTH, -1);
//					dataJson.put("$gte", sdf.format(calendar.getTime()));
//				}
//				if (filtro.getDistribuicaoDataAte() != null) {
//					dataJson.put("$lte", sdf.format(filtro.getDistribuicaoDataAte()));
//				}
//				iniciaisJson.put("distribuicaoData", dataJson);
//			}
//		}
//		if (!StringUtils.isEmpty(filtro.getNome())) {
//			HashMap<String, Object> regexJson = new HashMap<String, Object>();
//			regexJson.put("$regex", filtro.getNome());
//			regexJson.put("$options", "i");
//			HashMap<String, Object> parteJson = new HashMap<String, Object>();
//			parteJson.put("2", regexJson);
//			HashMap<String, Object> elementJson = new HashMap<String, Object>();
//			elementJson.put("$elemMatch", parteJson);
//			eventoJson.put("iniciais.partes", elementJson);
//		}
//		if (!iniciaisJson.isEmpty()) {
//			HashMap<String, Object> elementJson = new HashMap<String, Object>();
//			elementJson.put("$elemMatch", iniciaisJson);
//			eventoJson.put("iniciais", elementJson);
//		}
//
//		ObjectMapper mapper = new ObjectMapper();
//		String json = mapper.writeValueAsString(eventoJson);
//		String retorno = json.substring(1, json.length() - 1);
//		return StringUtils.isEmpty(retorno) ? retorno : "," + retorno;
//	}
//
//	private void notExists(Map<String, Object> map, String campo) {
//		HashMap<String, Object> falseOption = new HashMap<String, Object>();
//		falseOption.put(campo, false);
//		HashMap<String, Object> nullOption = new HashMap<String, Object>();
//		nullOption.put(campo, null);
//		HashMap<String, Object> notExistsMap = new HashMap<String, Object>();
//		notExistsMap.put("$exists", false);
//		HashMap<String, Object> notExistsOption = new HashMap<String, Object>();
//		notExistsOption.put(campo, notExistsMap);
//		HashMap<String, Object> orMap = new HashMap<String, Object>();
//		orMap.put("$or", new ArrayList<>(Arrays.asList(nullOption, notExistsOption, falseOption)));
//	}
}
