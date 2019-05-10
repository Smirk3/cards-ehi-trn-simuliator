package ehi.message;

import ehi.card.Card;
import ehi.card.exception.CardNotFoundException;
import ehi.merchant.exception.MerchantNotFoundException;
import ehi.merchant.model.Merchant;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;

public class Util {

    public static Card findCard(List<Card> cards, String cardPcId) throws CardNotFoundException {
        if (!StringUtils.hasText(cardPcId) || CollectionUtils.isEmpty(cards)) {
            throw new CardNotFoundException();
        }

        Optional<Card> card = cards.stream().filter(c -> cardPcId.equals(c.pcId)).findAny();

        if (card == null || !card.isPresent()){
            throw new CardNotFoundException();
        }

        return card.get();
    }

    public static Merchant findMerchant(List<Merchant> merchants, String merchantName) throws MerchantNotFoundException {
        if (!StringUtils.hasText(merchantName) || CollectionUtils.isEmpty(merchants)) {
            throw new MerchantNotFoundException();
        }

        Optional<Merchant> merchant = merchants.stream().filter(m -> merchantName.equals(m.name)).findAny();

        if (merchant == null || !merchant.isPresent()) {
            throw new MerchantNotFoundException();
        }

        return merchant.get();
    }

    public static String randomNumberInRange(int from, int to) {
        return Integer.valueOf(new SplittableRandom().nextInt(from, to)).toString();
    }
}
