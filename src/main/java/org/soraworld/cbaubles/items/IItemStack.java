package org.soraworld.cbaubles.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IItemStack {

    @Nullable
    Bauble getBauble();

    void setBauble(Bauble bauble);

    @Nonnull
    Bauble getOrCreateBauble();

}
