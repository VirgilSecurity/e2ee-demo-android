/*
 * Copyright (c) 2015-2018, Virgil Security, Inc.
 *
 * Lead Maintainer: Virgil Security Inc. <support@virgilsecurity.com>
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     (1) Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 *
 *     (2) Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *     (3) Neither the name of virgil nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.android.virgilsecurity.jwtworkexample.data.virgil;

import com.android.virgilsecurity.jwtworkexample.data.model.exception.KeyGenerationException;
import com.virgilsecurity.sdk.cards.Card;
import com.virgilsecurity.sdk.cards.CardManager;
import com.virgilsecurity.sdk.cards.model.RawCardContent;
import com.virgilsecurity.sdk.cards.model.RawSignedModel;
import com.virgilsecurity.sdk.cards.validation.CardVerifier;
import com.virgilsecurity.sdk.client.exceptions.VirgilServiceException;
import com.virgilsecurity.sdk.crypto.CardCrypto;
import com.virgilsecurity.sdk.crypto.VirgilCardCrypto;
import com.virgilsecurity.sdk.crypto.VirgilCrypto;
import com.virgilsecurity.sdk.crypto.VirgilKeyPair;
import com.virgilsecurity.sdk.crypto.exceptions.CryptoException;
import com.virgilsecurity.sdk.jwt.contract.AccessTokenProvider;
import com.virgilsecurity.sdk.storage.PrivateKeyStorage;

import java.util.List;

/**
 * Created by Danylo Oliinyk on 3/23/18 at Virgil Security.
 * -__o
 */

public class VirgilHelper {

    private final CardManager cardManager;

    public VirgilHelper(InitCardCrypto initCardCrypto,
                        InitAccessTokenProvider initAccessTokenProvider,
                        InitCardVerifier initCardVerifier) {

        cardManager = initCardManager(initCardCrypto, initAccessTokenProvider, initCardVerifier);
    }

    private CardManager initCardManager(InitCardCrypto initCardCrypto,
                                        InitAccessTokenProvider initAccessTokenProvider,
                                        InitCardVerifier initCardVerifier) {
        return new CardManager.Builder()
                .setCrypto(initCardCrypto.initialize())
                .setAccessTokenProvider(initAccessTokenProvider.initialize())
                .setCardVerifier(initCardVerifier.initialize())
                .build();
    }

    public Card publishCard(String identity) throws CryptoException, VirgilServiceException {
        VirgilKeyPair keyPair = generateKeyPair();
        RawSignedModel cardModel = cardManager.generateRawCard(keyPair.getPrivateKey(),
                                                               keyPair.getPublicKey(),
                                                               identity);
        return cardManager.publishCard(cardModel);
    }

    public Card getCard(String cardId) throws CryptoException, VirgilServiceException {
        return cardManager.getCard(cardId);
    }

    public List<Card> searchCards(String identity) throws CryptoException, VirgilServiceException {
        return cardManager.searchCards(identity);
    }

    public VirgilKeyPair generateKeyPair() {
        try {
            return getVirgilCrypto().generateKeys();
        } catch (CryptoException e) {
            e.printStackTrace();
            throw new KeyGenerationException(e);
        }
    }

    public VirgilCrypto getVirgilCrypto() {
        return ((VirgilCardCrypto) cardManager.getCrypto()).getVirgilCrypto();
    }

    public CardManager getCardManager() {
        return cardManager;
    }

    public interface InitCardCrypto {
        CardCrypto initialize();
    }

    public interface InitAccessTokenProvider {
        AccessTokenProvider initialize();
    }

    public interface InitCardVerifier {
        CardVerifier initialize();
    }
}
