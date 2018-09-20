package io.blocko.ethsearch.service;

import io.blocko.ethsearch.domain.Wallet;
import io.blocko.ethsearch.repository.WalletRepository;
import io.blocko.ethsearch.repository.search.WalletSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Wallet.
 */
@Service
@Transactional
public class WalletService {

    private final Logger log = LoggerFactory.getLogger(WalletService.class);

    private final WalletRepository walletRepository;

    private final WalletSearchRepository walletSearchRepository;

    public WalletService(WalletRepository walletRepository, WalletSearchRepository walletSearchRepository) {
        this.walletRepository = walletRepository;
        this.walletSearchRepository = walletSearchRepository;
    }

    /**
     * Save a wallet.
     *
     * @param wallet the entity to save
     * @return the persisted entity
     */
    public Wallet save(Wallet wallet) {
        log.debug("Request to save Wallet : {}", wallet);        Wallet result = walletRepository.save(wallet);
        walletSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the wallets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Wallet> findAll(Pageable pageable) {
        log.debug("Request to get all Wallets");
        return walletRepository.findAll(pageable);
    }


    /**
     * Get one wallet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Wallet> findOne(Long id) {
        log.debug("Request to get Wallet : {}", id);
        return walletRepository.findById(id);
    }

    /**
     * Delete the wallet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Wallet : {}", id);
        walletRepository.deleteById(id);
        walletSearchRepository.deleteById(id);
    }

    /**
     * Search for the wallet corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Wallet> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Wallets for query {}", query);
        return walletSearchRepository.search(queryStringQuery(query), pageable);    }
}
