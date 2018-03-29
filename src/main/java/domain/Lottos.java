package domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lottos {

    private static final int FORTH_PRIZE = 5000;
    private static final int THIRD_PRIZE = 50000;
    private static final int SECOND_PRIZE = 1500000;
    private static final int FIRST_PRIZE = 2000000000;
    private static final int ticketCost = 1000;

    private List<Lotto> lottos;

    private static final List<Integer> prizes = Arrays.asList(FORTH_PRIZE, THIRD_PRIZE, SECOND_PRIZE, FIRST_PRIZE);

    private Lottos() {

    }

    private Lottos(List<Lotto> lottos) {
        this.lottos = lottos;
    }

    public static Lottos of(){
        return new Lottos();
    }

    public static Lottos initLottosAuto(int n) {
        return new Lottos(IntStream.range(0, n).mapToObj(i -> Lotto.of(LottoUtil.getRandNumbers())).collect(Collectors.toList()));
    }

    public void addLottosManual(List<Integer> numbers){
        this.lottos.add(Lotto.of(numbers));
    }

    public int getMatchLottos(List<Integer> answer, int matchNum) {
        return (int) lottos.stream().filter(factor -> factor.getNumOfMatches(answer) == matchNum).count();
    }

    public List<Integer> getResult(List<Integer> answer) {
        return IntStream.range(3, 7).mapToObj(i -> getMatchLottos(answer, i)).collect(Collectors.toList());
    }

    public String toString() {
        return lottos.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }

    public int calcProfit(List<Integer> answer) {
        int totalPrize = IntStream.range(0, 4).map(i -> prizes.get(i) * getResult(answer).get(i)).sum();
        int bettingMoney = ticketCost * lottos.size();
        return (totalPrize - bettingMoney) / bettingMoney * 100;
    }
}
