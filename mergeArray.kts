fun insert_to_array(nums: IntArray, index: Int, value: Int) {
    if (value == 0) {
        return
    }
    val temp = nums[index]
    nums[index] = value
    insert_to_array(nums, index+1, temp)
}

fun merge(nums1: IntArray, m: Int, nums2: IntArray, n:Int) {
    var idx = 0
    for (num in nums2) {
        while (idx < nums1.size) {
            if (nums1[idx] == 0 || num < nums1[idx]) {
                insert_to_array(nums1, idx, num)
                idx++
                break
            }
            idx++
        }
    }
}

val nums1 = intArrayOf(1,2,3,0,0,0)
val nums2 = intArrayOf(2,5,6)
merge(nums1, 3, nums2, 3)
nums1.forEach { print("$it, ") }