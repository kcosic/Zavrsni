using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Review
    {
        public static ReviewDTO ToDTO(Review item, int level)
        {
            if (item == null || level <= 0)
            {
                return null;
            }

            return new ReviewDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Shop = Shop.ToDTO(item.Shop, level - 1),
                User = User.ToDTO(item.User, level - 1),
                UserId = item.UserId,
                ShopId = item.ShopId,
                Comment = item.Comment,
                Rating = item.Rating
            };
        }

        public static ICollection<ReviewDTO> ToListDTO(ICollection<Review> list, int level)
        {
            if (list == null || level <= 0)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(level)).ToList();
        }

        public ReviewDTO ToDTO(int level)
        {
            return level > 0 ? new ReviewDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Shop = Shop.ToDTO(Shop, level - 1),
                User = User.ToDTO(User, level - 1),
                UserId = UserId,
                ShopId = ShopId,
                Comment = Comment,
                Rating = Rating
            } : null;
        }
    }
}