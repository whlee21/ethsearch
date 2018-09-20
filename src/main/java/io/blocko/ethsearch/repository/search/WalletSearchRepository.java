package io.blocko.ethsearch.repository.search;

import io.blocko.ethsearch.domain.Wallet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Wallet entity.
 */
public interface WalletSearchRepository extends ElasticsearchRepository<Wallet, Long> {
}
