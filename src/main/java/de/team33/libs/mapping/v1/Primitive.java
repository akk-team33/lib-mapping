package de.team33.libs.mapping.v1;

class Primitive implements Normal
{

  private final Object origin;

  Primitive(final Object origin)
  {
    this.origin = origin;
  }

  @Override
  public final int hashCode()
  {
    return origin.hashCode();
  }

  @Override
  public final boolean equals(final Object obj)
  {
    return (this == obj) || ((obj instanceof Primitive) && origin.equals(((Primitive)obj).origin));
  }

  @Override
  public final String toString()
  {
    return origin.toString();
  }
}
