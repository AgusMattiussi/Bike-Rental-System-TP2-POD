package model;

import java.io.Serializable;

public record Pair<A, B>(A first, B second) implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!first.equals(pair.first)) return false;
        return second.equals(pair.second);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pair: (");
        sb.append(first);
        sb.append(", ").append(second);
        sb.append(')');
        return sb.toString();
    }
}
