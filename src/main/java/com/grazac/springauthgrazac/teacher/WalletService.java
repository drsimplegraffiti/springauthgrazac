package com.grazac.springauthgrazac.teacher;

import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    // page = cuurent page, size = limit, so
    //                                                                             id              desc
    public Page<Wallet> getWallets(WalletFilter filter, int page, int size, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort); // page 1 = 10, size = 2 ==> page 1, 2 recor
        Specification<Wallet> spec = WalletSpecification.filter(filter);
        return walletRepository.findAll(spec, pageable);
    }

//    @PostConstruct
//    private void insertAll(){
//        walletRepository.deleteAll();
//        for (int i = 1; i < 1001; i++) {
//            Wallet wallet = Wallet.builder()
//                    .name("my" + i + "name")
//                    .subject("sub" + i + "subject")
//                    .age(i + 3)
//                    .salary((double) (i + 100))
//                    .createdAt(LocalDate.now())
//                    .build();
//            walletRepository.save(wallet);
//        }
//    }
}
