##[面试题 17.14. 最小K个数](https://leetcode-cn.com/problems/smallest-k-lcci/)

~~~java
//大根堆实现
public int[] _smallestK(int[] arr, int k) {
        if (k == 0 || k > arr.length)
            return new int[0];
        PriorityQueue<Integer> queue = new PriorityQueue<>((x, y) -> {
            return y - x;
        });
        for (int i = 0; i < arr.length; i++) {
            if (queue.size() < k) {
                queue.add(arr[i]);
            } else {
                if (queue.peek() > arr[i]) {
                    queue.poll();
                    queue.offer(arr[i]);
                }
            }
        }
        int[] res = new int[k];
        for (int i = 0; i < k; i++)
            res[i] = queue.poll();
        return res;
    }

//快排思想实现
public void kth_elem(int[] a, int low, int high, int k) {
        int pivot = a[low];
        int low_temp = low, high_temp = high;
        while (low < high) {
            while (low < high && a[high] >= pivot)
                high--;
            a[low] = a[high];
            while (low < high && a[low] <= pivot)
                low++;
            a[high] = a[low];
        }
        a[low] = pivot;
        if (k == low)
            return;
        if (low > k)
            kth_elem(a, low_temp, low - 1, k);
        else
            kth_elem(a, low + 1, high_temp, k);
    }

    public int[] smallestK(int arr[], int k) {
        if (k == 0 || arr.length < k)
            return new int[0];
        kth_elem(arr, 0, arr.length - 1, k);
        int res[] = new int[k];
        for (int i = 0; i < k; i++)
            res[i] = arr[i];
        return res;
    }