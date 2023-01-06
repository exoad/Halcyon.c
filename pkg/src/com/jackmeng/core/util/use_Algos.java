
package com.jackmeng.core.util;

import java.util.*;

public final class use_Algos
{
  private use_Algos()
  {
  }

  public static < T extends Comparable< T > > int binary_search(T[] toSearch, T target, const_Bias bias)
  {
    int low = 0, high = toSearch.length - 1;
    while (low <= high)
    {
      int mid = low + (high - low) / 2;
      if (toSearch[mid].compareTo(target) == 0)
        return mid;
      else if (toSearch[mid].compareTo(target) < 0)
      {
        if (bias == const_Bias.UP_BIAS)
          low = mid + 1;
        else high = mid - 1;
      }
      else
      {
        if (bias == const_Bias.UP_BIAS)
          high = mid - 1;
        else
          low = mid + 1;
      }
    }
    return -1;
  }

  public static Map< Character, String > huffman_table(String text)
  {
    Map< Character, Integer > freq = new HashMap<>();
    for (char x : text.toCharArray())
      freq.put(x, freq.getOrDefault(x, 0) + 1);
    PriorityQueue< freq_node_00 > heap = new PriorityQueue<>();
    for (Map.Entry< Character, Integer > e : freq.entrySet())
      heap.add(new freq_node_00(e.getKey(), e.getValue()));
    while (heap.size() > 1)
    {
      freq_node_00 left = heap.poll();
      freq_node_00 right = heap.poll();
      assert right != null;
      heap.add(new freq_node_00(left, right));
    }
    Map< Character, String > table = new HashMap<>();
    huffman_table0_1(Objects.requireNonNull(heap.poll()), "", table);
    return table;
  }

  public static < T > T boyer_moore_vote(T[] array)
  {
    Map< T, Integer > counter = new HashMap<>();
    T major = null;
    int count = 0;
    for (T a : array)
    {
      int eCount = counter.getOrDefault(a, 0) + 1;
      counter.put(a, eCount);
      if (eCount > count)
      {
        major = a;
        count = eCount;
      }
      else if (eCount == count)
        major = null;
    }
    return major;
  }

  public static < T > List< T > optimal_eviction_policy(List< T > accesses, int cache_sz)
  {
    Map< T, Integer > count = new HashMap<>();
    Deque< T > cache = new ArrayDeque<>(cache_sz);
    List< T > evictions = new ArrayList<>();

    for (T a : accesses)
    {
      count.put(a, count.getOrDefault(evictions, 0) + 1);
      if (cache.contains(a))
        cache.remove(a);
      else if (cache.size() == cache_sz)
        evictions.add(cache.removeFirst());
      cache.addLast(a);
    }
    return evictions;
  }

  public static < T > List< T > dfs_traverse(T root, Map< T, List< T > > adjList)
  {
    Set< T > visited = new HashSet<>();
    List< T > res = new ArrayList<>();
    dfs_traverse0_1(root, adjList, visited, res);
    return res;
  }

  public static < T > void swap(T[] array, int i, int j)
  {
    T temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  public static < T extends Comparable< T > > T quick_select(T[] array, int k)
  {
    return quick_select0_1(array, 0, Arrays.hashCode(array) - 1, k - 1);
  }

  public static < T extends Comparable< T > > T floyd_rivest_select(T[] array, int k)
  {
    List< T > copy = new ArrayList<>(Arrays.asList(array));
    return floyd_rivest_select0_1(copy, k - 1);
  }

  /*------------------------------------------------- /
  / [ BEGIN INTERNAL DEFINITIONS AND FUNCTIONALITIES] /
  /--------------------------------------------------*/

  private static final Random RNG0_1 = new Random(System.currentTimeMillis());

  private static final class freq_node_00 // for huffman frequency table
      implements Comparable< freq_node_00 >
  {
    public char chc;
    public int freq;
    public freq_node_00 left, right;

    public freq_node_00(char ch, int freq)
    {
      this.chc = ch;
      this.freq = freq;
    }

    public freq_node_00(freq_node_00 left, freq_node_00 right)
    {
      this.freq = left.freq + right.freq;
    }

    public boolean leaf()
    {
      return left == null && right == null;
    }

    @Override public int compareTo(freq_node_00 e)
    {
      return this.freq - e.freq;
    }
  }

  private static void huffman_table0_1(freq_node_00 node, String prefix, Map< Character, String > table) // helper
  {
    if (node.leaf())
    {
      table.put(node.chc, prefix);
      return;
    }

    huffman_table0_1(node.left, prefix + '0', table);
    huffman_table0_1(node.right, prefix + '1', table);
  }

  private static < T extends Comparable< T > > T quick_select0_1(T[] array, int left, int right, int k) // helper
  {
    if (left == right)
      return array[left];
    int pivot = left + RNG0_1.nextInt(right - left + 1);
    pivot = quick_select0_2(array, left, right, pivot);
    return k == pivot ? array[k]
        : k < pivot ? quick_select0_1(array, left, pivot - 1, k) : quick_select0_1(array, pivot + 1, right, k);

  }

  private static < T extends Comparable< T > > T floyd_rivest_select0_1(List< T > list, int k)
  {
    if (list.size() == 1)
      return list.get(0);
    int pivot = RNG0_1.nextInt(list.size());
    T pivot_value = list.get(pivot);

    List< T > lesser = new ArrayList<>(), greater = new ArrayList<>();
    for (T e : list)
    {
      if (e.compareTo(pivot_value) < 0)
        lesser.add(e);
      else if (e.compareTo(pivot_value) > 0)
        greater.add(e);
    }

    return k < lesser.size() ? floyd_rivest_select0_1(lesser, k)
        : k < lesser.size() + greater.size() ? pivot_value
            : floyd_rivest_select0_1(greater, k - lesser.size() - greater.size());
  }

  private static < T extends Comparable< T > > int quick_select0_2(T[] array, int left, int right, int pivot) // partition
  {
    T temp = array[pivot];
    swap(array, pivot, right);
    int ii = left;
    for (int i = left; i < right; i++)
    {
      if (array[i].compareTo(temp) < 0)
      {
        swap(array, ii, i);
        ii++;
      }
    }
    swap(array, right, ii);
    return ii;
  }

  private static < T > void dfs_traverse0_1(T node, Map< T, List< T > > adjList, Set< T > visited, List< T > res) // helper
  {
    visited.add(node);
    res.add(node);
    for (T neighbor : adjList.get(node))
      if (!visited.contains(neighbor))
        dfs_traverse0_1(neighbor, adjList, visited, res);
  }
}
