package com.grazac.springauthgrazac.teacher;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/") //: ?name="shayo"&subject=math
    public Page<Wallet> getAllWallets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String subject,
            @RequestParam(defaultValue = "10", required = false) int size,  //limit or size
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "asc", required = false) String sortDir,
            @RequestParam(defaultValue = "id", required = false) String sortBy
){
        WalletFilter filter = new WalletFilter();
        filter.setName(name);
        filter.setSubject(subject);
        return walletService.getWallets(filter, page, size, sortBy, sortDir);
    }
}

