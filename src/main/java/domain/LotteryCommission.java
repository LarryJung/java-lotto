package domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static domain.LottoUtils.calcTotalEarnings;

public class LotteryCommission {

    private int round;
    private WinningLotto winningLotto;

    public LotteryCommission(int round) {
        this.round = round;
    }

    public void selectWinningNumbers(LottoNoGroup lottoNoGroup, LottoNo bonusNum) {
        winningLotto = WinningLotto.of(lottoNoGroup, bonusNum);
        round++;
    }

    private List<Rank> checkRanks(List<Lotto> lottos) {
        return lottos.stream().map(i -> i.askRank(winningLotto)).collect(Collectors.toList());
    }

    public List<Integer> informResults(List<Lotto> lottos) {
        return LottoUtils.rankToInt(checkRanks(lottos));
    }

    public int giveEarnings(List<Integer> prizeStatistics) {
        return calcTotalEarnings(prizeStatistics);
    }

    public WinningLotto getWinningLotto() {
        return winningLotto;
    }

    public int getRound() {
        return round;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LotteryCommission that = (LotteryCommission) o;
        return round == that.round &&
                Objects.equals(winningLotto, that.winningLotto);
    }

    @Override
    public int hashCode() {

        return Objects.hash(round, winningLotto);
    }
}
