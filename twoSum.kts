fun twoSum(nums: IntArray, target: Int): Pair<Int, Int>? {
    val nums2: MutableMap<Int, Int> = mutableMapOf()
    for ((index, num) in nums.withIndex()) {
        val completion = target - num
        if (nums2.containsKey(completion)) {
            return Pair(nums2.get(completion)!!, index)
        }
        else {
            nums2.put(num, index)
        }
    }
    return null
}
println(twoSum(intArrayOf(1, 2, 3, 4), 6))