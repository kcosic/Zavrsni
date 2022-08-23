using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Review{
        public static ReviewDTO ToDTO(Review item, bool singleLevel = true)
        {
            return new ReviewDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Shop = singleLevel ? null : Shop.ToDTO(item.Shop),
                User = singleLevel ? null : User.ToDTO(item.User),
                UserId = item.UserId,
                ShopId = item.ShopId,
                Comment = item.Comment,
                Rating = item.Rating
            };
        }

        public static ICollection<ReviewDTO> ToListDTO(ICollection<Review> list, bool singleLevel = true)
        {
            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public ReviewDTO ToDTO(bool singleLevel = true)
        {
            return new ReviewDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Shop = singleLevel ? null : Shop.ToDTO(Shop),
                User = singleLevel ? null : User.ToDTO(User),
                UserId = UserId,
                ShopId = ShopId,
                Comment = Comment,
                Rating = Rating
            };
        }
    }
}