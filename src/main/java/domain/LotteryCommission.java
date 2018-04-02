package domain;

import java.util.List;
import java.util.stream.Collectors;

import static domain.LottoUtils.calcTotalEarnings;

public class LotteryCommission {

    private static WinningLotto winningLotto;

    public static void selectWinningNumbers(LottoNoGroup lottoNoGroup, LottoNo bonusNum) {
        winningLotto = WinningLotto.of(lottoNoGroup, bonusNum);
    }

    private static List<Rank> checkRanks(List<Lotto> lottos) {
        return lottos.stream().map(i -> i.askRank(winningLotto)).collect(Collectors.toList());
    }

    public static List<Integer> informResults(List<Lotto> lottos) {
        return LottoUtils.rankToInt(checkRanks(lottos));
    }

    public static int giveEarnings(List<Integer> prizeStatistics) {
        return calcTotalEarnings(prizeStatistics);
    }
}
