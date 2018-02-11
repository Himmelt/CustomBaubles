package org.soraworld.cbaubles.items;

import javax.annotation.Nonnull;

public interface IItemStack {

    @Nonnull
    Bauble getBauble();

    void setBauble(@Nonnull Bauble bauble);
}
