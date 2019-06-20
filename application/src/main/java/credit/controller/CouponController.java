package credit.controller;

import credit.Paths;
import credit.core.exception.CreditRuntimeException;
import credit.core.service.CouponService;
import credit.model.CouponDto;
import credit.model.CouponGenerateRequestDto;
import credit.model.CouponGenerateResponseDto;
import credit.model.CouponRedeemRequestDto;
import credit.model.CouponsGetResponseDto;
import credit.model.ItemDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = Paths.API + Paths.COUPON,
                produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Coupon endpoints",
     tags = "Coupon")
public class CouponController {

    private static final int MAX_COUPONS = 250;

    private CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @ApiOperation(value = "getCoupon")
    @RequestMapping(value = "/single/{publicId}",
                    method = RequestMethod.GET)
    public ResponseEntity<CouponDto> getCoupon(
            @PathVariable("publicId")
            final String publicId) {

        CouponDto couponDto = couponService.getCoupon(publicId);

        if (couponDto == null) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "No coupon " + publicId);
        }

        return ResponseEntity.ok(couponDto);
    }

    @ApiOperation(value = "getCoupons")
    @RequestMapping(value = "/{accountEditId}",
                    method = RequestMethod.GET)
    public ResponseEntity<CouponsGetResponseDto> getCoupons(
            @PathVariable("accountEditId")
            final String accountEditId) {

        List<CouponDto> coupons = couponService.getCoupons(accountEditId);

        return ResponseEntity.ok(new CouponsGetResponseDto(coupons));
    }

    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CouponGenerateResponseDto> generateCoupon(
            @RequestBody
            final CouponGenerateRequestDto couponGenerateRequestDto) {

        if (couponGenerateRequestDto.getQuantity() <= 0 || couponGenerateRequestDto.getQuantity() > MAX_COUPONS) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Max quantity = " + MAX_COUPONS);
        }

        int currentCouponCount = (int) couponService.getCouponCount(couponGenerateRequestDto.getAccountEditId());
        if (currentCouponCount >= new Long(MAX_COUPONS)) {
            throw new CreditRuntimeException(HttpStatus.BAD_REQUEST, "Max = " + MAX_COUPONS);
        }

        int quantity = Math.min(MAX_COUPONS - currentCouponCount, couponGenerateRequestDto.getQuantity());

        List<CouponDto> coupons = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            CouponDto couponDto = couponService.generateNewCoupon(couponGenerateRequestDto.getAccountEditId(),
                                                                  couponGenerateRequestDto.getItemPublicId(),
                                                                  couponGenerateRequestDto.getAmount(),
                                                                  couponGenerateRequestDto.getMessage());
            if (couponDto != null) {
                coupons.add(couponDto);
            }
        }

        return ResponseEntity.ok(new CouponGenerateResponseDto(coupons));
    }

    @RequestMapping(value = Paths.REDEEM,
                    method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> redeemCoupon(
            @RequestBody
            final CouponRedeemRequestDto couponRedeemRequestDto) {

        ItemDto itemDto = couponService.addCouponToAccount(couponRedeemRequestDto.getCouponPublicId(),
                                                           couponRedeemRequestDto.getAccountEditId());

        return ResponseEntity.ok(itemDto);
    }
}
