package com.anypluspay.account.domain.repository;

import com.anypluspay.account.domain.InnerAccount;

/**
 * @author wxj
 * 2023/12/23
 */
public interface InnerAccountRepository {

    /**
     * 保存
     * @param account
     * @return
     */
    String store(InnerAccount account);

    void reStore(InnerAccount account);

    InnerAccount load(String accountNo);

    InnerAccount lock(String accountNo);

    void delete(String accountNo);
}
