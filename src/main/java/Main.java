import domain.*;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static domain.LottoUtils.*;
import static spark.Spark.*;

public class Main {

    static User user;
    static LottoDAO lottoDAO;

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    private static Map<String, Object> buyLottoInput(Request req) {
        // 사용자 정보 데이터베이스 입력
            lottoDAO = LottoDAO.getInstance(); // singleton instance
            lottoDAO.insertUserInfo(initUser(req), req.queryParams("round"), req.queryParams("inputMoney"));
            lottoDAO.insertLottosInfo(initUser(req), req.queryParams("round"));
        // 사용자 정보 데이터베이스에서 다시 꺼내와서 출력
        try {
            Map<String, Object> input = new HashMap<>();
            User user = lottoDAO.findUserByNameAndRoundFromLottos(req.queryParams("userName"), req.queryParams("round"));
            input.put("user", user);
            return input;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, Object> matchLottoResult(Request req) {
        try {
            // 해당 라운드의 위닝넘버 정보를 저장
            lottoDAO.insertWinningLotto(req.queryParams("round"),
                                        req.queryParams("winningNumber"),
                                        req.queryParams("bonusNumber"));

            // 사용자 이름과 라운드 정보를 바탕으로 포상 계산
            user = lottoDAO.findUserByNameAndRoundFromLottos(req.queryParams("userName"), req.queryParams("round"));
            user.checkTotalResult(lottoDAO.findWinningNumberByRound(req.queryParams("round")));
            lottoDAO.updateUserInfo(user, req.queryParams("round"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        return result;
    }

    public static void main(String[] args) {
        port(8080);
        staticFiles.location("/template");
        get("/", (req, res) -> render(null, "index.html"));
        post("/buyLotto", (req, res) -> render(buyLottoInput(req), "show.html"));
        post("/matchLotto", (req, res) -> render(matchLottoResult(req), "result.html"));
    }
}
