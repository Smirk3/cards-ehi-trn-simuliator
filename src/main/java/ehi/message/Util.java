package ehi.message;

import ehi.card.Card;
import ehi.settings.CardNotFoundException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

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
}
