package org.cryptocoinpartners.schema;

import org.cryptocoinpartners.service.OrderService;
import org.joda.time.Instant;

import java.math.BigDecimal;


/**
 * @author Tim Olson
 */
@SuppressWarnings("UnusedDeclaration")
public class OrderBuilder {


    public OrderBuilder(Portfolio portfolio) {
        this.portfolio = portfolio;
    }


    public OrderBuilder(Portfolio portfolio, OrderService orderService) {
        this.orderService = orderService;
        this.portfolio = portfolio;
    }


    /** @param volume to create a sell order, use a negative volume */
    public GeneralOrderBuilder create(Listing listing, String volume) {
        return new GeneralOrderBuilder(listing,volume);
    }


    /** @param volume to create a sell order, use a negative volume */
    public GeneralOrderBuilder create(Listing listing, BigDecimal volume) {
        return new GeneralOrderBuilder(listing,volume);
    }


    /** @param volume to create a sell order, use a negative volume */
    public SpecificOrderBuilder create( Market market, BigDecimal volume ) {
        return new SpecificOrderBuilder(market,volume);
    }


    /** @param volumeCount to create a sell order, use a negative volumeCount */
    public SpecificOrderBuilder create( Market market, long volumeCount ) {
        return new SpecificOrderBuilder(market,volumeCount);
    }


    /** @param volume to create a sell order, use a negative volume */
    public SpecificOrderBuilder create( Market market, Amount volume ) {
        return new SpecificOrderBuilder(market,volume);
    }


    @SuppressWarnings("unchecked")
    public abstract class CommonOrderBuilder<T> {

        public T withFillType(Order.FillType fillType) {
            getOrder().setFillType(fillType);
            return (T) this;
        }
    
        public T withMarginType(Order.MarginType marginType) {
            getOrder().setMarginType(marginType);
            return (T) this;
        }
    
        public T withExpiration(Instant expiration) {
            getOrder().setExpiration(expiration);
            return (T) this;
        }
    
        public T withPanicForce(boolean force) {
            getOrder().setPanicForce(force);
            return (T) this;
        }
    
        public T withEmulation(boolean emulation) {
            getOrder().setEmulation(emulation);
            return (T) this;
        }


        /** This finalizes the Order and places it with the OrderService the Builder was constructed with. */
        public Order place() {
            if( orderService == null )
                throw new IllegalStateException("You must construct Order.Builder with an OrderService to use the place() method.");
            Order order = getOrder();
            orderService.placeOrder(order);
            return order;
        }


        /** The Order will be constructed but not placed with any OrderService. */
        public Order build() { return getOrder(); }


        public abstract Order getOrder();
    }
    

    public class GeneralOrderBuilder extends CommonOrderBuilder<GeneralOrderBuilder> {

        public GeneralOrderBuilder(Listing listing, BigDecimal volume) {
            order = new GeneralOrder(listing,volume);
        }


        public GeneralOrderBuilder(Listing listing, String volume) {
            order = new GeneralOrder(listing,volume);
        }


        public GeneralOrderBuilder withLimitPrice(String price) {
            order.setLimitPrice(DecimalAmount.of(price));
            return this;
        }


        public GeneralOrderBuilder withStopPrice(String price) {
            order.setStopPrice(DecimalAmount.of(price));
            return this;
        }


        public GeneralOrderBuilder withLimitPrice(BigDecimal price) {
            order.setLimitPrice(DecimalAmount.of(price));
            return this;
        }


        public GeneralOrderBuilder withStopPrice(BigDecimal price) {
            order.setStopPrice(DecimalAmount.of(price));
            return this;
        }


        public GeneralOrder getOrder() { return order; }
        
        
        private GeneralOrder order;
    }


    public class SpecificOrderBuilder extends CommonOrderBuilder<SpecificOrderBuilder> {
        
        
        public SpecificOrderBuilder(Market market, BigDecimal volume) {
            order = new SpecificOrder(portfolio,market,volume);
        }


        public SpecificOrderBuilder(Market market, Amount volume) {
            order = new SpecificOrder(portfolio,market,volume);
        }


        public SpecificOrderBuilder(Market market, long volumeCount) {
            order = new SpecificOrder(portfolio,market,volumeCount);
        }


        public SpecificOrderBuilder withLimitPriceCount(long price /* units in basis of Market's quote fungible */) {
            order.setLimitPriceCount(price);
            return this;
        }


        public SpecificOrderBuilder withStopPriceCount(long price /* units in basis of Market's quote fungible */) {
            order.setStopPriceCount(price);
            return this;
        }
        
        
        public SpecificOrderBuilder withLimitPrice( DiscreteAmount price ) {
            price.assertBasis(order.getMarket().getPriceBasis());
            return withLimitPriceCount(price.getCount());
        }


        public SpecificOrderBuilder withStopPrice( DiscreteAmount price ) {
            price.assertBasis(order.getMarket().getPriceBasis());
            return withStopPriceCount(price.getCount());
        }


        public SpecificOrder getOrder() { return order; }


        private SpecificOrder order;
    }


    private OrderService orderService;
    private Portfolio portfolio;
}
