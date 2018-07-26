package htmlunit_demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

public class ServiceProgress {
	/**
	 * Get service progress from Chinese version page (Cathay General Hospital)
	 * 
	 * @param hosarea: Hospital division
	 * @param sec: Time section
	 * @param room: room number
	 * @return The result page as XML
	 */
	public String getProgress(String hosarea, String sec, String room) {
		String pageXml = "";
		try {
			WebClient webClient = new WebClient();
			webClient.getOptions().setUseInsecureSSL(true);
			String url = "https://reg.cgh.org.tw/tw/reg/RealTimeTable.jsp";
			HtmlPage page = webClient.getPage(url);

			HtmlSelect selectArea = (HtmlSelect) page.getByXPath("//select[@name='hosarea']").get(0);
			HtmlSelect selectSec = (HtmlSelect) page.getByXPath("//select[@name='sec']").get(0);
			HtmlSelect selectRoom = (HtmlSelect) page.getByXPath("//select[@name='room']").get(0);

			selectArea.setSelectedAttribute("4", true);
			selectSec.setSelectedAttribute("2", true);
			selectRoom.setSelectedAttribute("003", true);

			HtmlImage button = (HtmlImage) page.getByXPath("//img[@src='img/59.gif']").get(0);
			HtmlPage resultPage = (HtmlPage) button.click();

			pageXml = resultPage.asXml();

			webClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageXml;
	}

	/**
	 * Get service progress from English version page (Cathay General Hospital)
	 * 
	 * @param divisionNo: Hospital division
	 * @param depNo: Department number
	 * @return The result page as XML
	 */
	public String getProgressEn(String divisionNo, String depNo) {
		String pageXml = "";
		try {
			WebClient webClient = new WebClient();
			webClient.getOptions().setUseInsecureSSL(true);
			String url = "https://reg.cgh.org.tw/en/reg/RealTimeTable.jsp";

			HtmlPage page = webClient.getPage(url);

			HtmlSelect selectDiv = (HtmlSelect) page.getByXPath("//select[@name='DivisionNo']").get(0);
			selectDiv.setSelectedAttribute(divisionNo, true);

			HtmlSelect selectDep = (HtmlSelect) page
					.getByXPath("//select[contains(@name, 'CostNo')][not(contains(@style,'display: none'))]").get(0);
			selectDep.setSelectedAttribute(depNo, true);

			HtmlForm form = (HtmlForm) page.getByXPath("//form[@name='form1']").get(0);

			HtmlElement button = (HtmlElement) page.createElement("button");
			button.setAttribute("type", "submit");
			form.appendChild(button);
			page = button.click();
			pageXml = page.asXml();

			webClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageXml;
	}

	/**
	 * Extract the data from progress checking page (english page)
	 * 
	 * @param xml: Web age XML
	 * @param roomNo: Room number
	 * @return Message of notified email
	 */
	public MsgVO extractDataEn(String xml, String roomNo) {
		MsgVO vo = new MsgVO();
		Document doc = Jsoup.parseBodyFragment(xml);
		Element ele = doc.getElementById("resultDiv");
		Elements datas = ele.child(1).getElementsByTag("td");
		for (int i = 0; i < datas.size(); i++) {
			if (datas.get(i).text().trim().equals("Preparing")) {
				vo.setMsg("Preparing");
				return vo;
			}
			String[] inArr = datas.get(i).text().split("[A-Z][a-z]+\\s?[A-Z|n]*[a-z]*：");
			if (inArr[1].trim().equals(roomNo)) {
				vo.setMsg("");
				vo.setRoomNo(roomNo);
				vo.setPhysician(inArr[2]);
				vo.setCurrNo(inArr[3]);
			}
		}
		System.out.println("Room: " + vo.getCurrNo() + ", Physician: " + vo.getPhysician() + ", NO: " + vo.getCurrNo());
		return vo;
	}

}
