class Solution:

    def insert_to_array(self, nums, idx, num):
        if num == 0:
            return
        temp = nums[idx]
        nums[idx] = num
        self.insert_to_array(nums, idx+1, temp)

    def merge(self, nums1: list, m: int, nums2: list, n: int) -> None:
        t_idx = 0 # keep track of nums1
        for num in nums2:
            while t_idx < len(nums1):
                if nums1[t_idx] == 0 or num < nums1[t_idx]:
                    self.insert_to_array(nums=nums1, idx=t_idx, num = num)
                    t_idx += 1
                    break
                t_idx += 1


solution = Solution()
solution.merge([1,2,3,0,0,0], 3, [2,5,6], 3)