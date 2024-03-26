package lotto.controller;

import lotto.domain.BonusBall;
import lotto.domain.LottoMachine;
import lotto.domain.LottoTicket;
import lotto.domain.Price;
import lotto.domain.WinningNumbers;
import lotto.domain.number_generator.ManualNumberGenerator;
import lotto.view.InputView;
import lotto.view.ResultView;

import java.util.List;

public class LottoController {

    private final InputView inputView;
    private final ResultView resultView;

    public LottoController(InputView inputView, ResultView resultView) {
        this.inputView = inputView;
        this.resultView = resultView;
    }

    public void start() {
        resultView.printInit();

        Price price = new Price(inputView.inputNumber()); //구입금액입력
        inputView.inputNextLine();

        int numbersOfLotto = price.calculateNumberOfLotto(); //로또 갯수 계샨

        resultView.printInputNumbersOfManualLotto();
        int numbersOfManualLotto = generateNumbersOfManualLotto(numbersOfLotto);
        int numbersOfAutoLotto = numbersOfLotto - numbersOfManualLotto;

        resultView.printInputManualLottoNumbers();
        LottoMachine lottoMachine = new LottoMachine();
        // 수동으로 구매한 로또 수만큼 로또 생성
        generateManualLotto(numbersOfManualLotto, lottoMachine);
        // 자동으로 구매한 로또 수만큼 로또 생성
        List<LottoTicket> lottoTickets = lottoMachine.generateLottoTickets(numbersOfAutoLotto);

        resultView.printEachNumbersOfLotto(numbersOfManualLotto, numbersOfAutoLotto);
        resultView.printLottoTickets(lottoTickets);
        resultView.printDoInputWinningNumbers();

        WinningNumbers winningNumbers = WinningNumbers.of(new ManualNumberGenerator(inputView).generate());

        resultView.printBonusBallNumber();

        resultView.printResult(
                lottoMachine
                , winningNumbers
                , price
                , new BonusBall(inputView.inputNumber())
        );
    }

    private int generateNumbersOfManualLotto(int numbersOfLotto) {
        int numbersOfManualLotto = inputView.inputNumber();
        if (numbersOfManualLotto > numbersOfLotto) {
            throw new IllegalArgumentException(String.format("수동으로 구매할 로또 수가 총 로또 수(%s)보다 많습니다.", numbersOfLotto));
        }

        if (numbersOfManualLotto < 0) {
            throw new IllegalArgumentException("수동으로 구매할 로또 수는 음수가 될 수 없습니다.");
        }
        inputView.inputNextLine();
        return numbersOfManualLotto;
    }

    private void generateManualLotto(int numbersOfManualLotto, LottoMachine lottoMachine) {
        for (int i = 0; i < numbersOfManualLotto; i++) {
            lottoMachine.addLottoTicket(new LottoTicket(new ManualNumberGenerator(inputView).generate()));
        }
    }

}
