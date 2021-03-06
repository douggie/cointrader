package org.cryptocoinpartners.schema;

import org.joda.time.Instant;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;


/**
 * A Fill represents some completion of an Order.  The volume of the Fill might be less than the requested volume of the
 * Order
 *
 * @author Tim Olson
 */
@Entity
public class Fill extends RemoteEvent {


    public Fill(SpecificOrder order, Instant time, Market market, long priceCount, long volumeCount ) {
        this.order = order;
        this.market = market;
        this.priceCount = priceCount;
        this.volumeCount = volumeCount;
    }


    public @ManyToOne SpecificOrder getOrder() { return order; }


    @ManyToOne
    public Market getMarket() { return market; }


    @Transient
    public Amount getPrice() { return new DiscreteAmount(priceCount,market.getPriceBasis()); }
    public long getPriceCount() { return priceCount; }

    @Transient
    public Amount getVolume() { return new DiscreteAmount(volumeCount,market.getVolumeBasis()); }
    public long getVolumeCount() { return volumeCount; }
    
    @Transient
	public Amount getCommission() {
		// TODO Auto-generated method stub
		return null;
	}

    public String toString() {
        return "Fill{" +
                       "order=" + order.getId() +
                       ", market=" + market +
                       ", price=" + getPrice() +
                       ", volume=" + getVolume() +
                       '}';
    }


    // JPA
    protected Fill() {}
    protected void setOrder(SpecificOrder order) { this.order = order; }
    protected void setMarket(Market market) { this.market = market; }
    protected void setPriceCount(long priceCount) { this.priceCount = priceCount; }
    protected void setVolumeCount(long volumeCount) { this.volumeCount = volumeCount; }


    private SpecificOrder order;
    private Market market;
    private long priceCount;
    private long volumeCount;

}
