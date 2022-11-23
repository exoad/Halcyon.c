package com.jackmeng.util;

import java.util.ArrayList;
import java.util.List;

import com.jackmeng.halcyon.abst.impl_ForYou;

public final class use_Struct
{
  private use_Struct()
  {
  }

  public static final class struct_Node< T >
  {
    public Object key;
    public List< struct_Node< ? > > partners;

    public struct_Node(T i)
    {
      key = i;
      partners = new ArrayList<>();
    }
  }

  /*------------------------------------------------------------ /
  / This class basically defines all sets of "primitive" structs /
  / or structures that can be used.                              /
  /-------------------------------------------------------------*/

  public static final class struct_Pair< A, B >
      implements impl_ForYou< String >
  {
    /*-------------------------------------------------------------------- /
    / dont care about JSE standards of making everything private           /
    / and immutable, this isn't rust. make it with easier modifiers        /
    / so the JavaC tiered compilation wouldnt struggle on making additonal /
    / constructs for public methods                                        /
    /---------------------------------------------------------------------*/
    public A first;
    public B second;

    /*------------------------------------------------- /
    / got this inspiration from c++ utility's std::pair /
    /--------------------------------------------------*/

    public struct_Pair(A first, B second)
    {
      this.first = first;
      this.second = second;
    }

    public String toString()
    {
      /*---------------------------------------------------------------------------------------------- /
      / defunct don't use, override this method when using this IN JAVA PURE ONLY MODE and not via LUA /
      / script                                                                                         /
      /-----------------------------------------------------------------------------------------------*/
      return this.hashCode() + "_PAIR_" + first.getClass().getSimpleName() + " + " + second.getClass().getSimpleName()
          + ":[" + first + "," + second + "]";
    }

    @Override
    public boolean equals(Object cum)
    {
      /*-------------------------------------------------------------------------------------- /
      / this shit prob shldnt do checks like this, as it concerns specific instanceof with     /
      / type struct_Pair with no wildcard which most likely on Java5 and below (shitty oracle) /
      / will always return false.                                                              /
      /---------------------------------------------------------------------------------------*/
      if (!(cum instanceof struct_Pair))
        return false;
      struct_Pair< ?, ? > makePair = (struct_Pair< ?, ? >) cum;
      return makePair.first.equals(first) && makePair.second.equals(second);
    }

    @Override
    public int hashCode()
    {
      /*------------------------------------------------------ /
      / stupid hashcode calculations on basis of Integer.BYTES /
      /-------------------------------------------------------*/
      return (((((1 << (Integer.BYTES * 8) + 1) - 1) & first.hashCode())
          ^ ((((1 << (Integer.BYTES * 8) + 1) - 1) & first.hashCode()) >> ((Integer.BYTES * 8) / 2))) << ((Integer.BYTES
              * 8) / 2))
          /*---------------------------------------------- /
          / this part probably doesn't work, idk why /
          /-----------------------------------------------*/
          | ((((1 << (Integer.BYTES * 8) + 1) - 1) & second.hashCode())
              ^ ((((1 << (Integer.BYTES * 8) + 1) - 1) & second.hashCode()) >> ((Integer.BYTES * 8) / 2)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void forYou(String e)
    {
      this.first = (A) e.split("_")[0];
      this.second = (B) e.split("_")[1];
    }
  }

  public static final class struct_Trio< A, B, C >
      implements impl_ForYou< String >
  {
    public A first;
    public B second;
    public C third;

    /*------------------------------------------------------------- /
    / realised tuples wouldn't really work in Java or would be an   /
    / pain in the ass to impl, so i guess a triple or a trio works. /
    /--------------------------------------------------------------*/

    public struct_Trio(A first, B second, C third)
    {
      this.first = first;
      this.second = second;
      this.third = third;
    }

    public Object[] to_array()
    {
      return use_Commons.is_generic(first.getClass()) || use_Commons.is_generic(second.getClass())
          || use_Commons.is_generic(third.getClass()) ? null : new Object[] { first, second, third };
    }

    @Override
    @SuppressWarnings("unchecked")
    public void forYou(String e)
    {
      this.first = (A) e.split("_")[0];
      this.second = (B) e.split("_")[1];
      this.third = (C) e.split("_")[2];
    }
  }
}
